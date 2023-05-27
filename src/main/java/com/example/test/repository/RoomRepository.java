package com.example.test.repository;

import com.example.test.entity.Room;
import com.example.test.repository.search.RoomSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomSearch {
}
