package com.example.test.controller;

import com.example.test.dto.MessageDto;
import com.example.test.dto.ReservationRequestDto;
import com.example.test.security.UserDetailsImpl;
import com.example.test.service.ReservationService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // 예약하기
    @PostMapping("/reservation/{roomId}")
    public ResponseEntity<MessageDto> createReservation(@PathVariable Long roomId, ReservationRequestDto reservationRequestDto, @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return reservationService.createReservation(roomId, reservationRequestDto, userDetails.getUser());
    }
}
