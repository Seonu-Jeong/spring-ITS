package org.sparta.its.domain.reservation.controller;

import java.time.LocalDate;
import java.util.List;

import org.sparta.its.domain.reservation.dto.ReservationRequest;
import org.sparta.its.domain.reservation.dto.ReservationResponse;
import org.sparta.its.domain.reservation.service.ReservationService;
import org.sparta.its.global.security.UserDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * create on 2025. 01. 10.
 * create by IntelliJ IDEA.
 *
 * 예약 관련 Controller.
 *
 * @author Jun Heo
 */
@RestController
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;

	/**
	 * 좌석 선택 API
	 *
	 * @param concertId {@link PathVariable} 콘서트 Id
	 * @param seatId {@link PathVariable} 좌석 Id
	 * @param userDetail {@link AuthenticationPrincipal} 유저 Id
	 * @return {@link ReservationResponse.SelectDto}
	 */
	@PreAuthorize("hasAuthority('USER')")
	@PostMapping("/concerts/{concertId}/seats/{seatId}/select")
	public ResponseEntity<ReservationResponse.SelectDto> selectSeat(
		@PathVariable Long concertId,
		@PathVariable Long seatId,
		@RequestParam LocalDate date,
		@AuthenticationPrincipal UserDetail userDetail) {

		ReservationResponse.SelectDto selectDto = reservationService.selectSeat(concertId, seatId, date,
			userDetail.getId());

		return ResponseEntity.status(HttpStatus.CREATED).body(selectDto);
	}

	/**
	 * 예약 완료 처리 API
	 *
	 * @param reservationId {@link PathVariable} 예약 Id
	 * @param userDetail {@link AuthenticationPrincipal} 유저 Id
	 * @return {@link ReservationResponse.CompleteDto}
	 */
	@PreAuthorize("hasAuthority('USER')")
	@PostMapping("/reservations/{reservationId}")
	public ResponseEntity<ReservationResponse.CompleteDto> completeReservation(
		@PathVariable Long reservationId,
		@AuthenticationPrincipal UserDetail userDetail) {

		ReservationResponse.CompleteDto completeDto = reservationService.completeReservation(reservationId,
			userDetail.getId());

		return ResponseEntity.status(HttpStatus.OK).body(completeDto);
	}

	/**
	 * 예약 취소 요청 API
	 *
	 * @param reservationId {@link PathVariable} 예약 Id
	 * @param userDetail {@link AuthenticationPrincipal} 유저 Id
	 * @param cancelDto {@link RequestBody} 취소 Dto
	 * {@link ReservationResponse.CancelDto}
	 */
	@PreAuthorize("hasAuthority('USER')")
	@PostMapping("/reservations/{reservationId}/cancel")
	public ResponseEntity<ReservationResponse.CancelDto> cancelReservation(
		@PathVariable Long reservationId,
		@AuthenticationPrincipal UserDetail userDetail,
		@RequestBody ReservationRequest.CancelDto cancelDto) {

		ReservationResponse.CancelDto responseDto = reservationService.cancelReservation(reservationId,
			userDetail.getId(), cancelDto);

		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}

	/**
	 * 예약 조회 API
	 *
	 * @param startDate {@link RequestParam} 공연 시작 시간
	 * @param endDate {@link RequestParam} 공연 끝나는 시간
	 * @param concertTitle {@link RequestParam} 공연 이름
	 * @param singer {@link RequestParam} 가수 이름
	 * @param pageable {@link RequestParam} 페이징
	 * @return {@link ReservationResponse.ReservationListDto}
	 */
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/reservations")
	public ResponseEntity<List<ReservationResponse.ReservationListDto>> getAllReservations(
		@RequestParam(required = false) LocalDate startDate,
		@RequestParam(required = false) LocalDate endDate,
		@RequestParam(required = false) String concertTitle,
		@RequestParam(required = false) String singer,
		@PageableDefault(value = 5) Pageable pageable) {

		List<ReservationResponse.ReservationListDto> reservations = reservationService.getReservations(startDate,
			endDate, concertTitle, singer, pageable);

		return ResponseEntity.status(HttpStatus.OK).body(reservations);
	}
}
