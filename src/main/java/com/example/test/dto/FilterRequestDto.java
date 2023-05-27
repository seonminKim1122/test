package com.example.test.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class FilterRequestDto {
    // for paging
    private int page;

    int size = 10;

    // for filtering
    private String category;

    private String region;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkIn;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOut;

    private int guests = 1;

    private int minPrice = 0;

    private int maxPrice = Integer.MAX_VALUE;

    private String roomType;

    private List<String> amenities;
}
