package com.example.test.repository.search;

import com.example.test.dto.FilterRequestDto;
import com.example.test.entity.QReservation;
import com.example.test.entity.QRoom;
import com.example.test.entity.Reservation;
import com.example.test.entity.Room;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class RoomSearchImpl extends QuerydslRepositorySupport implements RoomSearch {

    public RoomSearchImpl() {
        super(Room.class);
    }

    @Override
    public Page<Room> searchWithFilter(FilterRequestDto filterRequestDto) {

        QRoom room = QRoom.room;
        QReservation reservation = QReservation.reservation;
        JPQLQuery<Room> query = from(room);
        JPQLQuery<Reservation> subquery = from(reservation);

        // 카테고리
        if (filterRequestDto.getCategory() != null) {
            query.where(room.categories.contains(filterRequestDto.getCategory()));
        }

        // 지역
        if (filterRequestDto.getRegion() != null) {
            query.where(room.region.eq(filterRequestDto.getRegion()));
        }

        // 게스트 수
        query.where(room.capacity.goe(filterRequestDto.getGuests()));

        // 가격
        query.where(room.price.goe(filterRequestDto.getMinPrice()));
        query.where(room.price.loe(filterRequestDto.getMaxPrice()));

        // 룸 타임
        if (filterRequestDto.getRoomType() != null) {
            query.where(room.roomType.eq(filterRequestDto.getRoomType()));
        }

        // 편의시설
        if (filterRequestDto.getAmenities() != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for(String amenity : filterRequestDto.getAmenities()) {
                booleanBuilder.and(room.amenities.contains(amenity));
            }
            query.where(booleanBuilder);
        }

        // 체크인 & 체크아웃
        // 1. 만기일이 체크아웃날짜 이후여야 함(null 은 무한을 의미)
        // 2. 내가 예약하고자 하는 기간에 예약이 없는 방이어야 함
            /*
            select *
            from room
            where room_id not in (select distinct room_id
                                  from reservation
                                  where checkOut >= myCheckIn and checkIn <= myCheckOut)
             */
        if (filterRequestDto.getCheckIn() != null && filterRequestDto.getCheckOut() != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            booleanBuilder.or(room.expiredDate.goe(filterRequestDto.getCheckOut()));
            booleanBuilder.or(room.expiredDate.isNull());
            booleanBuilder.and(room.id.notIn(
                    subquery.select(reservation.room.id).distinct()
                            .where(reservation.checkOut.goe(filterRequestDto.getCheckIn()))
                            .where(reservation.checkIn.loe(filterRequestDto.getCheckOut()))
            ));
            query.where(booleanBuilder);
        }

        // 페이징
        Pageable pageable = PageRequest.of(filterRequestDto.getPage()-1, filterRequestDto.getSize());
        this.getQuerydsl().applyPagination(pageable, query);

        List<Room> rooms = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(rooms, pageable, count);
    }
}
