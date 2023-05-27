package com.example.test.entity;

import com.example.test.dto.RoomRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roomId")
    private Long id;

    private String title;

    private int price;

    private String region;

    private String city;

    private int capacity;

    private String roomType;

    private LocalDate expiredDate;

    @ElementCollection
    private List<String> amenities = new ArrayList<>();

    @ElementCollection
    private List<String> categories = new ArrayList<>();

    @ElementCollection
    private List<String> images = new ArrayList<>();

    @ManyToOne
    private User user;

    public Room(RoomRequestDto roomRequestDto, LocalDate expiredDate, User user) {
        this.title = roomRequestDto.getTitle();
        this.price = roomRequestDto.getPrice();
        this.region = roomRequestDto.getRegion();
        this.city = roomRequestDto.getCity();
        this.capacity = roomRequestDto.getCapacity();
        this.roomType = roomRequestDto.getRoomType();
        this.expiredDate = expiredDate;
        this.user = user;
    }
}
