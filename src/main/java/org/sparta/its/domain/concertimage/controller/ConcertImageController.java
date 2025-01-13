package org.sparta.its.domain.concertimage.controller;

import org.sparta.its.domain.concertimage.dto.ConcertImageRequest;
import org.sparta.its.domain.concertimage.dto.ConcertImageResponse;
import org.sparta.its.domain.concertimage.service.ConcertImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/concerts")
@RequiredArgsConstructor
public class ConcertImageController {

	private final ConcertImageService concertImageService;

	@PreAuthorize("hasAuthority('ADMIN')")
	@PatchMapping("/concertImages/{concertImageId}")
	public ResponseEntity<ConcertImageResponse.UpdateDto> updateConcertImage(
		@PathVariable Long concertImageId,
		@ModelAttribute ConcertImageRequest.UpdateDto updateDto) {
		ConcertImageResponse.UpdateDto response = concertImageService.updatedConcertImage(concertImageId,
			updateDto);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
