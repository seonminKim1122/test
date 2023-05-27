package com.example.test.dto;

import com.example.test.entity.Room;
import lombok.Getter;

@Getter
public class RoomResponseDto {

    private Long id;

    private String title;

    private String region;

    private String city;

    private String host;

    public RoomResponseDto(Room room) {
        this.id = room.getId();
        this.title = room.getTitle();
        this.region = room.getRegion();
        this.city = room.getCity();
        this.host = room.getUser().getNickname();
    }
}
