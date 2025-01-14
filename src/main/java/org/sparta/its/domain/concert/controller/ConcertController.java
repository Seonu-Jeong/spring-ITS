package org.sparta.its.domain.concert.controller;

import java.time.LocalDate;
import java.util.List;

import org.sparta.its.domain.concert.dto.ConcertRequest;
import org.sparta.its.domain.concert.dto.ConcertResponse;
import org.sparta.its.domain.concert.service.ConcertService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	 * @param createDto {@link ConcertResponse.CreateDto} {@link ModelAttribute} 생성 Dto 요청
	 * @return {@link ResponseEntity} HttpStatus 상태값과 body 응답 {@link ConcertResponse.CreateDto} 조회Dto 응답
	 */
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping
	public ResponseEntity<ConcertResponse.CreateDto> createConcert(
		@Valid @ModelAttribute ConcertRequest.CreateDto createDto) {

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
		@RequestParam(defaultValue = "DESC") String order,
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

	/**
	 * 콘서트 정보 수정
	 * @param concertId {@link PathVariable} 콘서트 고유 식별자
	 * @param updateDto {@link RequestBody} 수정 정보 Dto 요청
	 * @return {@link ResponseEntity} HttpStatus 상태 값과 {@link ConcertResponse.UpdateDto} 수정Dto 응답
	 */
	@PreAuthorize("hasAuthority('ADMIN')")
	@PatchMapping("/{concertId}")
	public ResponseEntity<ConcertResponse.UpdateDto> updateConcert(
		@PathVariable Long concertId,
		@RequestBody ConcertRequest.UpdateDto updateDto) {

		ConcertResponse.UpdateDto response = concertService.updatedConcert(concertId, updateDto);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * 콘서트 등록 현황 조회
	 * @param title {@link RequestParam} 콘서트 제목으로 검색
	 * @param startAt {@link RequestParam}  콘서트 시작 날짜로 검색
	 * @param endAt {@link RequestParam} 콘서트 종료 날짜로 검색
	 * @param order {@link RequestParam} 콘서트 정렬 방식 default 내림차순
	 * @param pageable {@link PageableDefault} 페이징 기본값 설정
	 * @return {@link ResponseEntity} HttpStatus 상태 값과 {@link ConcertResponse.StatisticsDto} 수정Dto 응답
	 */
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/statistics")
	public ResponseEntity<List<ConcertResponse.StatisticsDto>> getStatistics(
		@RequestParam(required = false) String title,
		@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDate startAt,
		@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDate endAt,
		@RequestParam(defaultValue = "DESC") String order,
		@PageableDefault Pageable pageable) {
		List<ConcertResponse.StatisticsDto> response = concertService.getStatistics(title, startAt, endAt,
			order, pageable);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
