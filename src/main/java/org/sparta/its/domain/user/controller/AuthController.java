package org.sparta.its.domain.user.controller;

import static org.sparta.its.global.security.JwtUtil.*;

import org.sparta.its.domain.user.Service.UserService;
import org.sparta.its.domain.user.dto.AuthRequest;
import org.sparta.its.domain.user.dto.AuthResponse;
import org.sparta.its.global.security.JwtUtil;
import org.sparta.its.global.security.UserDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final JwtUtil jwtUtil;

	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse.SignUpDto> signup(
		@RequestBody AuthRequest.SignUpDto signUpDto
	) {

		AuthResponse.SignUpDto responseDto = userService.signUp(signUpDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse.LoginDto> login(
		@RequestBody AuthRequest.LoginDto loginDto,
		HttpServletResponse res
	) {

		AuthResponse.LoginDto responseDto = userService.login(loginDto);

		// JWT 토큰 생성
		String token = jwtUtil.createToken(
			responseDto.getId(),
			responseDto.getEmail(),
			responseDto.getName(),
			responseDto.getRole());

		// 쿠키에 토큰 등록
		jwtUtil.addJwtToCookie(token, res);

		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}

	@PostMapping("/logout")
	public ResponseEntity<AuthResponse.LogoutDto> logout(
		@AuthenticationPrincipal UserDetail userDetail,
		HttpServletResponse response
	) {

		// JWT 삭제 비우기
		Cookie jwtCookie = new Cookie(AUTHORIZATION_HEADER, "");

		jwtCookie.setMaxAge(0);
		jwtCookie.setPath("/");

		response.addCookie(jwtCookie);

		// 응답 DTO 생성
		AuthResponse.LogoutDto responseDto = new AuthResponse.LogoutDto(
			userDetail.getId(),
			userDetail.getName()
		);

		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}

}
