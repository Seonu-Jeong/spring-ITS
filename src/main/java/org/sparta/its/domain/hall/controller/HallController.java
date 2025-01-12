package org.sparta.its.domain.hall.controller;

import java.util.List;

import org.sparta.its.domain.hall.dto.HallRequest;
import org.sparta.its.domain.hall.dto.HallResponse;
import org.sparta.its.domain.hall.service.HallService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/halls")
@RequiredArgsConstructor
public class HallController {

	private final HallService hallService;

	/**
	 * 공연장을 등록하는 API
	 * @param createDto {@link ModelAttribute} 이름, 지역, 수용 인원, 이미지 필수 값
	 * @return {@link ResponseEntity} httpStatus 와 {@link HallResponse.CreatDto} dto 응답
	 */
	@PostMapping
	public ResponseEntity<HallResponse.CreatDto> createHall(
		@Valid @ModelAttribute HallRequest.CreateDto createDto) {

		HallResponse.CreatDto creatDto
			= hallService.creatHall(createDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(creatDto);
	}

	/**
	 * 모든 공연장 조회하는 API
	 * @param name {@link RequestParam} 공연장 이름
	 * @param location {@link RequestParam} 공연장 위치
	 * @param pageable {@link RequestParam} page, size 파라미터
	 * @return {@link ResponseEntity} httpStatus 와 {@link HallResponse.ReadDto} 조회 dto 응답
	 */
	@GetMapping
	public ResponseEntity<List<HallResponse.ReadDto>> getHalls(
		@RequestParam(required = false) String name,
		@RequestParam(required = false) String location,
		@PageableDefault(value = 5) Pageable pageable) {

		List<HallResponse.ReadDto> halls = hallService.getHalls(name, location, pageable);

		return ResponseEntity.status(HttpStatus.OK).body(halls);
	}

	/**
	 * 공연장 상세 조회하는 API
	 * @param hallId {@link PathVariable} 공연장 고유 식별자
	 * @return {@link ResponseEntity} httpStatus 와 {@link HallResponse.ReadDto} 조회 dto 응답
	 */
	@GetMapping("/{hallId}")
	public ResponseEntity<HallResponse.ReadDto> getDetailHall(
		@PathVariable Long hallId) {

		HallResponse.ReadDto detailHall = hallService.getDetailHall(hallId);

		return ResponseEntity.status(HttpStatus.OK).body(detailHall);
	}

	/**
	 * 공연장 삭제하는 API
	 * @param hallId {@link PathVariable} 공연장 고유 식별자
	 * @return {@link ResponseEntity} httpStatus 와 {@link HallResponse.ReadDto} 조회 dto 응답
	 */
	@DeleteMapping("/{hallId}")
	public ResponseEntity<HallResponse.DeleteDto> deleteHall(
		@PathVariable Long hallId) {

		HallResponse.DeleteDto deleteDto = hallService.deleteHall(hallId);

		return ResponseEntity.status(HttpStatus.OK).body(deleteDto);
	}
}
