package com.example.test.repository.search;

import com.example.test.dto.FilterRequestDto;
import com.example.test.entity.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoomSearch {

    Page<Room> searchWithFilter(FilterRequestDto filterRequestDto);
}
