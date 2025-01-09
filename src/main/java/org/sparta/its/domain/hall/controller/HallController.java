package org.sparta.its.domain.hall.controller;

import java.io.IOException;

import org.sparta.its.domain.hall.dto.HallRequest;
import org.sparta.its.domain.hall.dto.HallResponse;
import org.sparta.its.domain.hall.service.HallService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
