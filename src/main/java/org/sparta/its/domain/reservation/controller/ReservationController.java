package org.sparta.its.domain.reservation.controller;

import org.sparta.its.domain.reservation.dto.ReservationResponse;
import org.sparta.its.domain.reservation.service.ReservationService;
import org.sparta.its.global.security.UserDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	 * @param userDetail {@link AuthenticationPrincipal}유저 ID
	 * @return {@link ResponseEntity} httpStatus 와 {@link ReservationResponse.SelectDto} 조회 dto 응답
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
	 * @return {@link ResponseEntity} httpStatus 와 {@link ReservationResponse.CompleteDto} 조회 dto 응답
	 */
	@PreAuthorize("hasAuthority('USER')")
	@PostMapping("/reservations/{reservationId}")
	public ResponseEntity<ReservationResponse.CompleteDto> completeReservation(
		@PathVariable Long reservationId,
		@AuthenticationPrincipal UserDetail userDetail) {
		ReservationResponse.CompleteDto completeDto = reservationService.completeReservation(reservationId, userDetail.getId());

		return ResponseEntity.status(HttpStatus.OK).body(completeDto);
	}

	/**
	 * 예약 취소 처리
	 *
	 * @param reservationId 예약 ID
	 * @param rejectComment 취소 사유
	 * @return ReservationResponse.CompleteDto 예약 완료된 정보
	 */
	@PreAuthorize("hasAuthority('USER')")
	@PostMapping("/reservations/{reservationId}")
	public ResponseEntity<ReservationResponse.CancelDto> cancelReservation(
		@PathVariable Long reservationId,
		@AuthenticationPrincipal UserDetail userDetail,
		@RequestBody ReservationRequest.CancelDto cancelDto) {

		ReservationResponse.CancelDto responseDto = reservationService.cancelReservation(reservationId, userDetail.getId(), cancelDto);

		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}
}
