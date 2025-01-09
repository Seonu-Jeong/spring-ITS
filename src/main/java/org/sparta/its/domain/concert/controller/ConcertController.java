package org.sparta.its.domain.concert.controller;

import org.sparta.its.domain.concert.dto.ConcertRequest;
import org.sparta.its.domain.concert.dto.ConcertResponse;
import org.sparta.its.domain.concert.service.ConcertService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/concerts")
@RequiredArgsConstructor
public class ConcertController {

	private final ConcertService concertService;

	/**
	 * 콘서트 등록
	 * @param createDto {@link ConcertResponse.CreateDto} {@link ModelAttribute} 생성 DTO 요청
	 * @return {@link ResponseEntity} HttpStatus 상태값과 body 응답
	 */
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping
	public ResponseEntity<ConcertResponse.CreateDto> createConcert(
		@Valid @ModelAttribute ConcertRequest.CreateDto createDto) {
		ConcertResponse.CreateDto response = concertService.createConcert(createDto);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
