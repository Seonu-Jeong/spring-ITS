package org.sparta.its.domain.reservation.controller;

import org.sparta.its.domain.reservation.dto.ReservationResponse;
import org.sparta.its.domain.reservation.service.ReservationService;
import org.sparta.its.domain.user.repository.UserRepository;
import org.sparta.its.global.security.UserDetail;
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
	 * @param userDetail {@link AuthenticationPrincipal}유저 ID
	 * @return ResponseEntity<ReservationResponse.SelectDto>
	 */
	@PreAuthorize("hasAuthority('USER')")
	@PostMapping("/select")
	public ResponseEntity<ReservationResponse.SelectDto> selectSeat(
		@PathVariable Long concertId,
		@PathVariable Long seatId,
		@AuthenticationPrincipal UserDetail userDetail
		) {
		ReservationResponse.SelectDto selectDto = reservationService.selectSeat(concertId, seatId, userDetail.getId());

		return ResponseEntity.status(HttpStatus.CREATED).body(selectDto);
	}

	/**
	 * 예약 완료 처리
	 *
	 * @param concertId {@link PathVariable}콘서트 ID
	 * @param seatId {@link PathVariable}좌석 ID
	 * @param reservationId {@link PathVariable}예약 ID
	 * @param userDetail {@link AuthenticationPrincipal}유저 ID
	 * @return ReservationResponse.CompleteDto 예약 완료된 정보
	 */
	@PreAuthorize("hasAuthority('USER')")
	@PostMapping("/reservations/{reservationId}")
	public ResponseEntity<ReservationResponse.CompleteDto> completeReservation(
		@PathVariable Long concertId,
		@PathVariable Long seatId,
		@PathVariable Long reservationId,
		@AuthenticationPrincipal UserDetail userDetail
	) {
		ReservationResponse.CompleteDto completeDto = reservationService.completeReservation(concertId, seatId, reservationId, userDetail.getId());

		return ResponseEntity.status(HttpStatus.OK).body(completeDto);
	}
}
