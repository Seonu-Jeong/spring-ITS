package org.sparta.its.domain.concert.controller;

import java.util.List;

import org.sparta.its.domain.concert.dto.ConcertRequest;
import org.sparta.its.domain.concert.dto.ConcertResponse;
import org.sparta.its.domain.concert.service.ConcertService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/concerts")
@RequiredArgsConstructor
public class ConcertController {

	private final ConcertService concertService;

	@PostMapping
	public ResponseEntity<ConcertResponse.ResponseDto> createConcert(
		@Valid @RequestPart ConcertRequest.CreateDto requestDto,
		@RequestPart List<MultipartFile> images) {
		ConcertResponse.ResponseDto response = concertService.createConcert(requestDto, images);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
