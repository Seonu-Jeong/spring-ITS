package org.sparta.its.domain.reservation.controller;

import java.nio.file.attribute.UserPrincipal;

import org.sparta.its.domain.reservation.dto.ReservationResponse;
import org.sparta.its.domain.reservation.service.ReservationService;
import org.sparta.its.domain.user.entity.User;
import org.sparta.its.domain.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
	private final UserRepository userRepository;

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
		@PathVariable Long seatId,
		@AuthenticationPrincipal User user
		) {
		ReservationResponse.SelectDto selectDto = reservationService.selectSeat(concertId, seatId, user);

		return ResponseEntity.status(HttpStatus.CREATED).body(selectDto);
	}

	/**
	 * 예약 완료 처리
	 *
	 * @param concertId 콘서트 ID
	 * @param seatId 좌석 ID
	 * @param reservationId 예약 ID
	 * @return ReservationResponse.CompleteDto 예약 완료된 정보
	 */
	@PreAuthorize("hasAuthority('USER')")
	@PostMapping("/reservations/{reservationId}")
	public ResponseEntity<ReservationResponse.CompleteDto> completeReservation(
		@PathVariable Long concertId,
		@PathVariable Long seatId,
		@PathVariable Long reservationId,
		@AuthenticationPrincipal User user
	) {
		ReservationResponse.CompleteDto completeDto = reservationService.completeReservation(concertId, seatId, reservationId, user);

		return ResponseEntity.status(HttpStatus.OK).body(completeDto);
	}
}
