package com.example.test.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoomRequestDto {

    private String title;

    private int price;

    private String region;

    private String city;

    private int capacity;

    private String roomType;

    private int expiredDate;

    private List<String> amenities;

    private List<String> categories;

    private List<MultipartFile> images;

}
