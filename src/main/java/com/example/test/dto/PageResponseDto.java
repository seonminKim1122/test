package com.example.test.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PageResponseDto<E> {

    private int page;
    private int size;
    private int total;

    // 이전 페이지의 존재 여부
    private boolean prev;
    // 다음 페이지의 존재 여부
    private boolean next;

    private List<E> dtoList;

    public PageResponseDto(FilterRequestDto filterRequestDto, List<E> dtoList, int total) {
        this.page = filterRequestDto.getPage();
        this.size = filterRequestDto.getSize();

        this.total = total;
        this.dtoList = dtoList;

        prev = this.page > 1;
        next = (this.page * this.size) < total;
    }
}
