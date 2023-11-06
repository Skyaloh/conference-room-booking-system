package com.mashreq.booking.service;


import com.mashreq.booking.domain.ConferenceRoom;
import com.mashreq.booking.dto.ConferenceRoomDTO;
import com.mashreq.booking.mapper.ConferenceRoomMapper;
import com.mashreq.booking.repository.ConferenceRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConferenceRoomService {

    private final Logger log = LoggerFactory.getLogger(ConferenceRoomService.class);

    private final ConferenceRoomRepository conferenceRoomRepository;

    private final ConferenceRoomMapper conferenceRoomMapper;

    public ConferenceRoomService(ConferenceRoomRepository conferenceRoomRepository, ConferenceRoomMapper conferenceRoomMapper) {
        this.conferenceRoomRepository = conferenceRoomRepository;
        this.conferenceRoomMapper = conferenceRoomMapper;
    }

    /**
     * Save a conferenceRoom.
     *
     * @param conferenceRoomDTO the entity to save
     * @return the persisted entity
     */
    public ConferenceRoomDTO save(ConferenceRoomDTO conferenceRoomDTO) {
        log.debug("Request to save ConferenceRoom : {}", conferenceRoomDTO);

        ConferenceRoom conferenceRoom = conferenceRoomMapper.toEntity(conferenceRoomDTO);
        conferenceRoom = conferenceRoomRepository.save(conferenceRoom);
        return conferenceRoomMapper.toDto(conferenceRoom);
    }


    /**
     * Delete the conferenceRoom by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ConferenceRoom : {}", id);
        conferenceRoomRepository.deleteById(id);
    }
}
