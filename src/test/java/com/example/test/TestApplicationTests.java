package com.example.test;

import com.example.test.dto.ReservationRequestDto;
import com.example.test.dto.RoomRequestDto;
import com.example.test.entity.Reservation;
import com.example.test.entity.Room;
import com.example.test.entity.User;
import com.example.test.repository.ReservationRepository;
import com.example.test.repository.RoomRepository;
import com.example.test.repository.UserRepository;
import com.example.test.service.ReservationService;
import com.example.test.service.RoomService;
import com.example.test.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class TestApplicationTests {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void conCurrencyTests() throws InterruptedException {
        User user1 = new User("test1", "test1", "test1");
        User user2 = new User("test2", "test2", "test2");

        userRepository.saveAndFlush(user1);
        userRepository.saveAndFlush(user2);

        RoomRequestDto roomRequestDto = new RoomRequestDto();
        roomRequestDto.setTitle("Room1");
        roomRequestDto.setPrice(20000);
        roomRequestDto.setRegion("Region1");
        roomRequestDto.setCity("City1");
        roomRequestDto.setCapacity(5);
        roomRequestDto.setRoomType("Type1");
        roomRequestDto.setExpiredDate(2);

        Room room = new Room(roomRequestDto, LocalDate.of(2023, 6, 20), user1);
        roomRepository.saveAndFlush(room);

        ReservationRequestDto reservationRequestDto1 = new ReservationRequestDto(LocalDate.of(2023, 6, 6), LocalDate.of(2023, 6, 10), 1);
        ReservationRequestDto reservationRequestDto2 = new ReservationRequestDto(LocalDate.of(2023, 6, 8), LocalDate.of(2023, 6, 15), 1);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        executorService.execute(() -> {
            try {
                reservationService.createReservation(1L, reservationRequestDto1, user1);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            latch.countDown();
        });

        executorService.execute(() -> {
            try {
                reservationService.createReservation(1L, reservationRequestDto2, user2);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            latch.countDown();
        });

        latch.await();

        List<Reservation> reservations = reservationRepository.findAllByRoom(room);
        Assertions.assertEquals(1, reservations.size());
    }
}
