package com.example.test.controller;

import com.example.test.dto.*;
import com.example.test.security.UserDetailsImpl;
import com.example.test.service.RoomService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping(value = "/rooms/host", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageDto> hostRoom(@ModelAttribute RoomRequestDto roomRequestDto, @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return roomService.hostRoom(roomRequestDto, userDetails);
    }

    @GetMapping("/rooms")
    public ResponseEntity<PageResponseDto<RoomResponseDto>> getRooms(@ModelAttribute FilterRequestDto filterRequestDto) {
        return roomService.getRooms(filterRequestDto);
    }
}
