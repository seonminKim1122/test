package com.example.test.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ReservationRequestDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkIn;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOut;

    private int guests;

    public ReservationRequestDto(LocalDate checkIn, LocalDate checkOut, int guests) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.guests = guests;
    }
}
