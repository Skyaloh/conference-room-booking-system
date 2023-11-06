package com.mashreq.booking.service;


import com.mashreq.booking.domain.Booking;
import com.mashreq.booking.domain.enumeration.BookingStatus;
import com.mashreq.booking.dto.BookingDTO;
import com.mashreq.booking.dto.ConferenceRoomDTO;
import com.mashreq.booking.mapper.BookingMapper;
import com.mashreq.booking.repository.BookingRepository;
import com.mashreq.booking.rest.errors.ConferenceRoomNotFound;
import com.mashreq.booking.rest.errors.InvalidBookingDateException;
import com.mashreq.booking.rest.errors.UnderMaintenanceException;
import com.mashreq.booking.rest.vm.BookingVM;
import com.mashreq.booking.service.criteria.BookingCriteria;
import com.mashreq.booking.service.criteria.ConferenceRoomCriteria;
import com.mashreq.booking.service.criteria.MaintenanceWindowCriteria;
import com.mashreq.booking.service.helper.filter.InstantFilter;
import com.mashreq.booking.service.helper.filter.IntegerFilter;
import com.mashreq.booking.service.helper.filter.LongFilter;
import com.mashreq.booking.service.queryservice.BookingQueryService;
import com.mashreq.booking.service.queryservice.ConferenceRoomQueryService;
import com.mashreq.booking.service.queryservice.MaintenanceWindowQueryService;
import com.mashreq.booking.util.TimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookingService {

    private final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final ConferenceRoomQueryService conferenceRoomQueryService;
    private final BookingQueryService bookingQueryService;

    private final BookingMapper bookingMapper;

    private final MaintenanceWindowQueryService maintenanceWindowQueryService;

    public BookingService(BookingRepository bookingRepository, ConferenceRoomQueryService conferenceRoomQueryService, BookingQueryService bookingQueryService, BookingMapper bookingMapper, MaintenanceWindowQueryService maintenanceWindowQueryService) {
        this.bookingRepository = bookingRepository;
        this.conferenceRoomQueryService = conferenceRoomQueryService;
        this.bookingQueryService = bookingQueryService;
        this.bookingMapper = bookingMapper;
        this.maintenanceWindowQueryService = maintenanceWindowQueryService;
    }

    public BookingDTO bookRoom(BookingVM bookingVM){


        Instant startTime = TimeFormatter.parseDateTime(bookingVM.getStartTime());
        Instant endTime = TimeFormatter.parseDateTime(bookingVM.getEndTime());

        //Check if the booking is for the current day
        ZonedDateTime currentDate = Instant.now().atZone(ZoneId.systemDefault());
        if(startTime.atZone(ZoneId.systemDefault()).getDayOfMonth() != currentDate.getDayOfMonth() ||
                endTime.atZone(ZoneId.systemDefault()).getDayOfMonth() != currentDate.getDayOfMonth()){
            throw new InvalidBookingDateException(bookingVM.getStartTime());
        }

        ConferenceRoomCriteria conferenceRoomCriteria = new ConferenceRoomCriteria();
        conferenceRoomCriteria.setCapacity(new IntegerFilter());
        conferenceRoomCriteria.getCapacity().setGreaterThanOrEqual(bookingVM.getNumberOfPeople());

        // find all the rooms with capacity greater than or equal to the number of people
        List<ConferenceRoomDTO> conferenceRoomsList = conferenceRoomQueryService.findByCriteria(conferenceRoomCriteria);
        Optional<ConferenceRoomDTO> targetRoom = conferenceRoomsList.stream().min(Comparator.comparing(ConferenceRoomDTO::getCapacity));

        if(targetRoom.isEmpty()){
            throw new ConferenceRoomNotFound("No conference room found with capacity greater than or equal to " + bookingVM.getNumberOfPeople()+ " for the given time");
        }


        ConferenceRoomDTO conferenceRoomDTO = targetRoom.get();

        // check if the room is under maintenance
        checkRoomsMaintenanceWindow(conferenceRoomDTO, startTime, endTime, bookingVM);

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setStartTime(startTime);
        bookingDTO.setEndTime(endTime);
        bookingDTO.setNumberOfPeople(bookingVM.getNumberOfPeople());
        bookingDTO.setUserId(bookingVM.getUserId());

        BookingCriteria bookingCriteria = getBookingCriteria(bookingDTO, conferenceRoomDTO.getId());

        // check if the room is available for the given time
        if(bookingQueryService.countByCriteria(bookingCriteria) > 0){

            // if not available, remove the room from the list and book the next optimal room
           conferenceRoomsList.remove(conferenceRoomDTO);
           return bookNextOptimalRoom(bookingDTO, conferenceRoomsList, bookingVM);
        } else{
            bookingDTO.setConferenceRoomId(conferenceRoomDTO.getId());
            return save(bookingDTO);
        }


    }


    // Check if the room is under maintenance
    private void checkRoomsMaintenanceWindow(ConferenceRoomDTO conferenceRoomDTO, Instant startTime, Instant endTime,BookingVM bookingVM){
        if(startTime != null && endTime != null){

            MaintenanceWindowCriteria maintenanceWindowCriteria = new MaintenanceWindowCriteria();
            maintenanceWindowCriteria.setConferenceRoomId(new LongFilter());
            maintenanceWindowCriteria.getConferenceRoomId().setEquals(conferenceRoomDTO.getId());

            LocalTime localStartTime = startTime.atZone(ZoneId.systemDefault()).toLocalTime();
            LocalTime localEndTime = endTime.atZone(ZoneId.systemDefault()).toLocalTime();

            maintenanceWindowQueryService.findByCriteria(maintenanceWindowCriteria).forEach(maintenanceWindowDTO -> {
                if(localStartTime.equals(maintenanceWindowDTO.getStartTime()) ||
                        (localStartTime.isAfter(maintenanceWindowDTO.getStartTime()) && localStartTime.isBefore(maintenanceWindowDTO.getEndTime()))){
                    throw new UnderMaintenanceException(bookingVM.getStartTime());
                }
                if(localEndTime.equals(maintenanceWindowDTO.getEndTime()) ||
                        (localEndTime.isAfter(maintenanceWindowDTO.getStartTime()) && localEndTime.isBefore(maintenanceWindowDTO.getEndTime()))){
                    throw new UnderMaintenanceException(bookingVM.getEndTime());
                }
            });

        }
    }

    //Booking the next optimal room
    private BookingDTO bookNextOptimalRoom(BookingDTO bookingDTO, List<ConferenceRoomDTO> conferenceRoomsList, BookingVM bookingVM) {
        Optional<ConferenceRoomDTO> nextOptimalConferenceRoom =
                conferenceRoomsList.stream().min(Comparator.comparing(ConferenceRoomDTO::getCapacity));

        if(nextOptimalConferenceRoom.isEmpty()){
            throw new ConferenceRoomNotFound("No conference room found with capacity greater than or equal to " + bookingDTO.getNumberOfPeople() + " for the given time");
        }else {
            ConferenceRoomDTO conferenceRoomDTO = nextOptimalConferenceRoom.get();

            // check if the room is under maintenance
            checkRoomsMaintenanceWindow(conferenceRoomDTO, bookingDTO.getStartTime(), bookingDTO.getEndTime(), bookingVM);

            bookingDTO.setConferenceRoomId(conferenceRoomDTO.getId());
            return save(bookingDTO);
        }
    }


    private static BookingCriteria getBookingCriteria(BookingDTO bookingDTO, Long conferenceRoomId) {
        BookingCriteria bookingCriteria = new BookingCriteria();
        bookingCriteria.setConferenceRoomId(new LongFilter());
        bookingCriteria.getConferenceRoomId().setEquals(conferenceRoomId);
        bookingCriteria.setStartTime(new InstantFilter());
        bookingCriteria.getStartTime().setGreaterThanOrEqual(bookingDTO.getStartTime());
        bookingCriteria.setEndTime(new InstantFilter());
        bookingCriteria.getEndTime().setLessThanOrEqual(bookingDTO.getEndTime());
        bookingCriteria.setStatus(new BookingCriteria.BookingStatusFilter());
        bookingCriteria.getStatus().setEquals(BookingStatus.BOOKED);
        return bookingCriteria;
    }

    /**
     * Save a booking.
     *
     * @param bookingDTO the entity to save
     * @return the persisted entity
     */
    public BookingDTO save(BookingDTO bookingDTO) {
        log.debug("Request to save Booking : {}", bookingDTO);

        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking = bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }


    /**
     * Delete the booking by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Booking : {}", id);
        bookingRepository.deleteById(id);
    }
}
