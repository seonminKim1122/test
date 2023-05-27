package com.example.test.service;

import com.example.test.dto.*;
import com.example.test.entity.Room;
import com.example.test.repository.RoomRepository;
import com.example.test.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {

    @Value("${com.example.upload.path}")
    private String uploadPath;

    private final RoomRepository roomRepository;

    @Transactional
    public ResponseEntity<MessageDto> hostRoom(RoomRequestDto roomRequestDto, UserDetailsImpl userDetails) {
        LocalDate expiredDate = calcExpiredDate(roomRequestDto.getExpiredDate());

        Room room = new Room(roomRequestDto, expiredDate, userDetails.getUser());

        for (String amenity : roomRequestDto.getAmenities()) {
            room.getAmenities().add(amenity);
        }

        for (String category : roomRequestDto.getCategories()) {
            room.getCategories().add(category);
        }

        List<String> imgPaths = fileUploader(roomRequestDto.getImages());
        for (String imgPath : imgPaths) {
            room.getImages().add(imgPath);
        }

        roomRepository.save(room);
        return ResponseEntity.ok(new MessageDto("숙소 등록에 성공했습니다."));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<PageResponseDto<RoomResponseDto>> getRooms(FilterRequestDto filterRequestDto) {
        Page<Room> rooms = roomRepository.searchWithFilter(filterRequestDto);
        PageResponseDto<RoomResponseDto> responseDto = new PageResponseDto<>(
                filterRequestDto,
                rooms.getContent().stream().map(RoomResponseDto::new).toList(),
                (int)rooms.getTotalElements()
        );

        return ResponseEntity.ok(responseDto);
    }



    // 로컬 저장소에 파일들을 업로드 하고 그 경로들을 반환하는 메서드
    private List<String> fileUploader(List<MultipartFile> files) { // 추후 S3 에 이미지 저장하도록 변경(현재는 내 로컬 저장소)
        List<String> uploadedPaths = new ArrayList<>();
        if (files != null) {
            files.forEach(multipartFile -> {
                String originalName = multipartFile.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();

                Path savePath = Paths.get(uploadPath, uuid+"_"+originalName);
                try {
                    multipartFile.transferTo(savePath);
                    uploadedPaths.add(uploadPath + uuid + "_" + originalName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        return uploadedPaths;
    }

    // 숙소 등록 시 호스트가 지정한 만기일자를 계산하는 메서드
    private LocalDate calcExpiredDate(int expiredDate) {
        LocalDate result = LocalDate.now();
        switch (expiredDate) {
            case 1:
                result = result.plusMonths(12L);
                break;
            case 2:
                result = result.plusMonths(9L);
                break;
            case 3:
                result = result.plusMonths(6L);
                break;
            case 4:
                result = result.plusMonths(3L);
                break;
            default: // 향후 모든 날짜(null 로 DB에 저장 후 필터링 단에서 null 이면 무한으로 인식)
                result = null;
        }
        return result;
    }
}
