package org.sparta.its.domain.concert.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.sparta.its.domain.concert.dto.ConcertRequest;
import org.sparta.its.domain.concert.dto.ConcertResponse;
import org.sparta.its.domain.concert.service.ConcertService;
import org.sparta.its.global.exception.ConcertException;
import org.sparta.its.global.exception.errorcode.ConcertErrorCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
	 * @return {@link ResponseEntity} HttpStatus 상태값과 body 응답 {@link ConcertResponse.CreateDto} 조회Dto 응답
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

		if (createDto.getStartAt().isBefore(LocalDateTime.now()) || createDto.getEndAt()
			.isBefore(LocalDateTime.now())) {
			throw new ConcertException(ConcertErrorCode.ALREADY_PASSED);
		}
		//TODO : 콘서트 등록 시 일자 확인 : 현재 시간을 기준으로 이전일 경우 예외 처리
		ConcertResponse.CreateDto response = concertService.createConcert(createDto);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * 콘서트 가수명 및 콘서트명으로 다건 조회
	 * @param singer {@link RequestParam} 가수명 검색 조건
	 * @param concertTitle {@link RequestParam} 콘서트 검색 조건
	 * @param order {@link RequestParam} 내림차순 or 오름차순 정렬
	 * @param pageable {@link PageableDefault} pageable 인터페이스 size 및 page default 값 설정
	 * @return {@link ResponseEntity} HttpStatus 상태 값과 {@link ConcertResponse.FindDto} 조회Dto 응답
	 */
	@GetMapping
	public ResponseEntity<List<ConcertResponse.FindDto>> findAll(
		@RequestParam(required = false) String singer,
		@RequestParam(required = false) String concertTitle,
		@RequestParam(defaultValue = "내림차순") String order,
		@PageableDefault Pageable pageable) {

		List<ConcertResponse.FindDto> allConcertDto = concertService.findAll(singer, concertTitle, order, pageable);

		return ResponseEntity.status(HttpStatus.OK).body(allConcertDto);
	}

	// @GetMapping("/{concertId}")
	// public ResponseEntity<ConcertResponse.FindDto> findConcert(
	// 	@PathVariable Long concertId) {
	// 	ConcertResponse.FindDto response = concertService.findedConcert(concertId);
	//
	// 	return ResponseEntity.status(HttpStatus.OK).body(response);
	// }
}
