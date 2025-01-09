package org.sparta.its.domain.hall.controller;

import java.io.IOException;

import org.sparta.its.domain.hall.dto.HallRequest;
import org.sparta.its.domain.hall.dto.HallResponse;
import org.sparta.its.domain.hall.service.HallService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	 * @param createDto 이름, 지역, 수용 인원, 이미지 필수 ㄱ밧
	 * @return
	 * @throws IOException
	 */
	@PostMapping
	public ResponseEntity<HallResponse.CreatDto> createHall(
		@Valid @ModelAttribute HallRequest.CreateDto createDto) {

		HallResponse.CreatDto creatDto
			= hallService.creatHall(createDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(creatDto);
	}

	/**
	 * 모든 공연장 조회
	 * @param name 공연장 이름
	 * @param location 공연장 위치
	 * @param pageable page, size 파라미터
	 * @return
	 */
	@GetMapping
	public ResponseEntity<Page<HallResponse.ReadDto>> getHalls(
		@RequestParam(required = false) String name,
		@RequestParam(required = false) String location,
		@PageableDefault(value = 5) Pageable pageable) {

		Page<HallResponse.ReadDto> halls = hallService.getHalls(name, location, pageable);

		return ResponseEntity.status(HttpStatus.OK).body(halls);
	}
}
