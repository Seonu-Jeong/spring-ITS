package org.sparta.its.domain.reservation.controller;

import org.sparta.its.domain.reservation.dto.ReservationResponse;
import org.sparta.its.domain.reservation.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/concerts/{concertId}/seats/{seatId}")
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;

	/**
	 * 좌석 선택
	 *
	 * @param concertId {@link PathVariable}콘서트 ID
	 * @param seatId {@link PathVariable}좌석 ID
	 * @return ResponseEntity<ReservationResponse.SelectDto>
	 */
	@PreAuthorize("hasAuthority('USER')")
	@PostMapping("/select")
	public ResponseEntity<ReservationResponse.SelectDto> selectSeat(
		@PathVariable Long concertId,
		@PathVariable Long seatId) {

		ReservationResponse.SelectDto selectDto = reservationService.selectSeat(concertId, seatId);

		return ResponseEntity.status(HttpStatus.CREATED).body(selectDto);
	}
}
