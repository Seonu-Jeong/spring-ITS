package org.sparta.its.domain.hallImage.controller;

import org.sparta.its.domain.hallImage.dto.HallImageRequest;
import org.sparta.its.domain.hallImage.dto.HallImageResponse;
import org.sparta.its.domain.hallImage.service.HallImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/halls/{hallId}")
@RequiredArgsConstructor
public class HallImageController {

	private final HallImageService hallImageService;

	/**
	 * 공연장 이미지 업데이트하는 API
	 * @param hallId {@link PathVariable} 공연장 고유 식별자
	 * @param updateImageDto createDto {@link ModelAttribute} 이미지 포멧팅, 공연장이미지 고유 식별자, 이미지
	 * @return {@link ResponseEntity} httpStatus 와 test
	 */
	@PatchMapping("/hallImages/{hallImagesId}")
	public ResponseEntity<HallImageResponse.UpdateDto> updateHallImage(
		@PathVariable Long hallId,
		@PathVariable Long hallImagesId,
		@Valid @ModelAttribute HallImageRequest.UpdateImageDto updateImageDto) {

		HallImageResponse.UpdateDto updateDto
			= hallImageService.updateHallImage(hallId, hallImagesId, updateImageDto);

		return ResponseEntity.status(HttpStatus.OK).body(updateDto);
	}
}
