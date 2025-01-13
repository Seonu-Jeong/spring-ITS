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
import org.springframework.web.bind.annotation.PathVariable;
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

		ConcertResponse.CreateDto response = concertService.createConcert(createDto);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * 콘서트 가수명 및 콘서트명으로 다건 조회
	 * @param singer {@link RequestParam} 가수명 검색 조건
	 * @param concertTitle {@link RequestParam} 콘서트 검색 조건
	 * @param order {@link RequestParam} 내림차순 or 오름차순 정렬
	 * @param pageable {@link PageableDefault} pageable 인터페이스 size 및 page default 값 설정
	 * @return {@link ResponseEntity} HttpStatus 상태 값과 {@link ConcertResponse.ReadDto} 조회Dto 응답
	 */
	@GetMapping
	public ResponseEntity<List<ConcertResponse.ReadDto>> getConcerts(
		@RequestParam(required = false) String singer,
		@RequestParam(required = false) String concertTitle,
		@RequestParam(defaultValue = "내림차순") String order,
		@PageableDefault Pageable pageable) {

		List<ConcertResponse.ReadDto> allConcertDto = concertService.getConcerts(singer, concertTitle, order, pageable);

		return ResponseEntity.status(HttpStatus.OK).body(allConcertDto);
	}

	/**
	 * 콘서트 상세 조회
	 * @param concertId {@link PathVariable} 콘서트 고유 식별자
	 * @return {@link ResponseEntity} HttpStatus 상태 값과 {@link ConcertResponse.ReadDto} 조회Dto 응답
	 * */
	@GetMapping("/{concertId}")
	public ResponseEntity<ConcertResponse.ReadDto> getDetailConcert(
		@PathVariable Long concertId) {
		ConcertResponse.ReadDto response = concertService.getDetailConcert(concertId);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
