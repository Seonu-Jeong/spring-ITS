package org.sparta.its.domain.user.controller;

import org.sparta.its.domain.user.Service.UserService;
import org.sparta.its.domain.user.dto.UserRequest;
import org.sparta.its.domain.user.dto.UserResponse;
import org.sparta.its.global.security.UserDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	/**
	 * 유저 수정 API
	 * @param updateDto {@link Valid} {@link RequestBody} 유저 수정 DTO
	 * @param userDetail {@link AuthenticationPrincipal} 유저 인증 객체
	 * @return {@link ResponseEntity} HttpStatus 상태값과 body 응답 {@link UserResponse.UpdateDto} 수정 Dto 응답
	 */
	@PatchMapping
	public ResponseEntity<UserResponse.UpdateDto> updateUser(
		@Valid @RequestBody UserRequest.UpdateDto updateDto,
		@AuthenticationPrincipal UserDetail userDetail) {
		UserResponse.UpdateDto responseDto = userService.updateUser(updateDto, userDetail.getId());

		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}

	/**
	 * 유저 회원탈퇴 API
	 * @param deleteDto {@link Valid} {@link RequestBody} 유저 삭제 DTO
	 * @param userDetail {@link AuthenticationPrincipal} 유저 인증 객체
	 * @return {@link ResponseEntity} HttpStatus 상태값과 body 응답 {@link UserResponse.DeleteDto} 삭제 Dto 응답
	 */
	@DeleteMapping
	public ResponseEntity<UserResponse.DeleteDto> deleteUser(
		@Valid @RequestBody UserRequest.DeleteDto deleteDto,
		@AuthenticationPrincipal UserDetail userDetail) {

		UserResponse.DeleteDto responseDto = userService.deleteUser(deleteDto, userDetail.getId());

		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}
}