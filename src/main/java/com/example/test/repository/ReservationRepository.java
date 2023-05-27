package com.example.test.repository;

import com.example.test.entity.Reservation;
import com.example.test.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("select r from Reservation r where r.room = :room and r.checkOut >= :checkIn and r.checkIn <= :checkOut")
    List<Reservation> findAllAffectMyPlan(@Param("room")Room room, @Param("checkIn")LocalDate checkIn, @Param("checkOut")LocalDate checkOut);

    List<Reservation> findAllByRoom(Room room);
}
