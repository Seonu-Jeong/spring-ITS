package org.sparta.its.domain.reservation.entity.controller;

import org.sparta.its.domain.reservation.entity.dto.ReservationResponseDto;
import org.sparta.its.domain.reservation.entity.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/concerts/{concertId}")
public class ReservationController {

	private ReservationService reservationService;

	@PostMapping("/seats/{seatId}/select")
	public ResponseEntity<ReservationResponseDto> selectSeat(
		@PathVariable Long concertId,
		@PathVariable Long seatId) {

		try{
			ReservationResponseDto responseDto = reservationService.selectSeat(concertId, seatId);
			return ResponseEntity.ok(responseDto);
		} catch (IllegalStateException e){
			return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
		}
	}
}
