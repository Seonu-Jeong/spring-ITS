package org.sparta.its.domain.reservation.controller;

import org.sparta.its.domain.reservation.dto.ReservationRequest;
import org.sparta.its.domain.reservation.dto.ReservationResponse;
import org.sparta.its.domain.reservation.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/concerts/{concertId}/seats/{seatId}")
public class ReservationController {

	private final ReservationService reservationService;

	/**
	 * 좌석 선택
	 *
	 * @param selectDto 좌석 선택 요청 DTO
	 // * @return ResponseEntity<ReservationResponse.SelectDto>
	 */
	@PreAuthorize("hasAuthority('USER')")
	@PostMapping("/select")
	public ResponseEntity<ReservationResponse.SelectDto> selectSeat(
		@Valid @PathVariable ReservationRequest.SelectDto selectDto) {
		ReservationResponse.SelectDto responseDto
			= reservationService.selectSeat(selectDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}
}
