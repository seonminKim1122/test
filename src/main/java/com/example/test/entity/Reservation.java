package com.example.test.entity;

import com.example.test.dto.ReservationRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserveId")
    private Long id;

    private LocalDate checkIn;

    private LocalDate checkOut;

    private int guests;

    @ManyToOne
    private User user;

    @ManyToOne
    private Room room;

    public Reservation(ReservationRequestDto reservationRequestDto, Room room, User user) {
        this.checkIn = reservationRequestDto.getCheckIn();
        this.checkOut = reservationRequestDto.getCheckOut();
        this.guests = reservationRequestDto.getGuests();
        this.user = user;
        this.room = room;
    }
}
