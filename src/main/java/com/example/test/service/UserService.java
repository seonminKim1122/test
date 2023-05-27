package com.example.test.service;

import com.example.test.dto.LoginRequestDto;
import com.example.test.dto.MessageDto;
import com.example.test.dto.SignupRequestDto;
import com.example.test.entity.User;
import com.example.test.repository.UserRepository;
import com.example.test.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public ResponseEntity<MessageDto> signup(SignupRequestDto signupRequestDto) {

        Optional<User> found = userRepository.findByEmail(signupRequestDto.getEmail());
        if (found.isPresent())  {
            throw new IllegalArgumentException("이미 회원가입한 이메일입니다.");
        }

        User user = new User(signupRequestDto);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageDto("회원 가입 성공"));
    }

    public ResponseEntity<MessageDto> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        Optional<User> found = userRepository.findByEmail(loginRequestDto.getEmail());

        if(found.isEmpty()) {
            throw new IllegalArgumentException("가입하지 않은 이메일입니다.");
        }

        if (!found.get().getPassword().equals(loginRequestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(loginRequestDto.getEmail()));
        return ResponseEntity.ok(new MessageDto("로그인 성공"));
    }
}
