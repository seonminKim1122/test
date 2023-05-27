package com.example.test.service;

import com.example.test.dto.MessageDto;
import com.example.test.dto.ReservationRequestDto;
import com.example.test.entity.Reservation;
import com.example.test.entity.Room;
import com.example.test.entity.User;
import com.example.test.repository.ReservationRepository;
import com.example.test.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public ResponseEntity<MessageDto> createReservation(Long roomId, ReservationRequestDto reservationRequestDto, User user) {

        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new NullPointerException("존재하지 않는 방입니다.")
        );

        if(room.getCapacity() < reservationRequestDto.getGuests()) {
            throw new IllegalArgumentException("숙소 가용 인원을 초과했습니다.");
        }

        synchronized (this) { // 동기화 블록으로 만들어 2명이 동시 예약을 시도할 경우 1명이 먼저 예약되고 나머지 1명은 예약이 안 되도록 막아야 함
            if(!isAvailable(room, reservationRequestDto.getCheckIn(), reservationRequestDto.getCheckOut())) {
                throw new IllegalArgumentException("이미 예약이 있는 날짜입니다.");
            }

            Reservation reservation = new Reservation(reservationRequestDto, room, user);
            reservationRepository.save(reservation);
            return ResponseEntity.ok(new MessageDto("예약 성공"));
        }
    }

    private boolean isAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        List<Reservation> reservations = reservationRepository.findAllAffectMyPlan(room, checkIn, checkOut);
        return reservations.size() == 0;
    }
}
