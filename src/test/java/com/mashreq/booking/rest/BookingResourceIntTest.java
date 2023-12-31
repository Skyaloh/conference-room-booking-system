package com.mashreq.booking.rest;


import com.mashreq.booking.IntegrationTest;
import com.mashreq.booking.domain.Booking;
import com.mashreq.booking.repository.BookingRepository;
import com.mashreq.booking.rest.errors.ExceptionTranslator;
import com.mashreq.booking.rest.vm.BookingVM;
import com.mashreq.booking.service.BookingService;
import com.mashreq.booking.service.queryservice.BookingQueryService;
import com.mashreq.booking.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class BookingResourceIntTest {


    private static final Long DEFAULT_USER_ID = 1L;
    private static final int DEFAULT_NUMBER_OF_PEOPLE = 7;
    private MockMvc restMockMvc;

    private static final Instant DEFAULT_START_TIME = Instant.now();
    private static final  Instant DEFAULT_END_TIME = Instant.now().plus( 15,ChronoUnit.MINUTES);


    @Autowired
    private BookingService bookingService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;


    @Autowired
    private BookingQueryService bookingQueryService;


    @Autowired
    private BookingRepository bookingRepository;

    private BookingVM bookingVM;

    @Autowired
    private ExceptionTranslator exceptionTranslator;


    public static BookingVM createBookingVM(){

        BookingVM bookingVM = new BookingVM();
        bookingVM.setEndTime(TestUtil.formatInstantTime(DEFAULT_END_TIME));
        bookingVM.setStartTime(TestUtil.formatInstantTime(DEFAULT_START_TIME));
        bookingVM.setNumberOfPeople(DEFAULT_NUMBER_OF_PEOPLE);
        bookingVM.setUserId(DEFAULT_USER_ID);
        return bookingVM;
    }

    @BeforeEach
    public void init() {
        bookingVM = createBookingVM();
    }
    @BeforeEach
    public void initTest() {
        MockitoAnnotations.openMocks(this);
        final BookingResource bookingResource = new BookingResource(bookingService,bookingQueryService);
        this.restMockMvc =
                MockMvcBuilders
                        .standaloneSetup(bookingResource)
                        .setCustomArgumentResolvers(pageableArgumentResolver)
                        .setControllerAdvice(exceptionTranslator)
                        .setMessageConverters(jacksonMessageConverter)
                        .build();
    }


    @Test
    @Transactional
    void createBooking() throws Exception {
        int databaseSizeBeforeCreate = bookingRepository.findAll().size();

        // Create the Booking
        restMockMvc
                .perform(
                        post("/api/bookings")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(TestUtil.convertObjectToJsonBytes(bookingVM))
                )
                .andExpect(status().isCreated());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeCreate + 1);
        Booking testBooking = bookingList.get(bookingList.size() - 1);
        assertThat(testBooking.getNumberOfPeople()).isEqualTo(DEFAULT_NUMBER_OF_PEOPLE);
    }

    @Test
    @Transactional
    void createBookingWith_Incorrect_Date_Fails() throws Exception {
        int databaseSizeBeforeCreate = bookingRepository.findAll().size();

        bookingVM.setStartTime(TestUtil.formatInstantTime(DEFAULT_END_TIME.plus(1,ChronoUnit.DAYS)));

        // Create the Booking
        restMockMvc
                .perform(
                        post("/api/bookings")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(TestUtil.convertObjectToJsonBytes(bookingVM))
                )
                .andExpect(status().isInternalServerError());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeCreate);
    }
}
