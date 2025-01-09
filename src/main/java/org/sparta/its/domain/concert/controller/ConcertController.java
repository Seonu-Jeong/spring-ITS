package org.sparta.its.domain.concert.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.sparta.its.domain.concert.dto.ConcertRequest;
import org.sparta.its.domain.concert.dto.ConcertResponse;
import org.sparta.its.domain.concert.service.ConcertService;
import org.sparta.its.global.exception.ConcertException;
import org.sparta.its.global.exception.errorcode.ConcertErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

		if (createDto.getRunningStartTime().isAfter(createDto.getRunningEndTime())) {
			throw new ConcertException(ConcertErrorCode.IS_NOT_AFTER_TIME);
		}

		if (createDto.getStartAt().isAfter(createDto.getEndAt())) {
			throw new ConcertException(ConcertErrorCode.IS_NOT_AFTER_DATE);
		}
		//TODO : 콘서트 등록 시 일자 확인
		ConcertResponse.CreateDto response = concertService.createConcert(createDto);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping
	public ResponseEntity<List<ConcertResponse.FindDto>> findAll(
		@RequestParam(required = false) String singer,
		@RequestParam(required = false) String concertTitle,
		@RequestParam(required = false) String order,
		@RequestParam(defaultValue = "0") Integer page,
		@RequestParam(defaultValue = "10") Integer size,
		@RequestParam(required = false) LocalDateTime startAt) {
		List<ConcertResponse.FindDto> allConcertDto = concertService.findAll(singer, concertTitle, order, page, size,
			startAt);

		return ResponseEntity.status(HttpStatus.OK).body(allConcertDto);
	}
}
