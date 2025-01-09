package org.sparta.its.domain.reservation.service;

import org.sparta.its.domain.reservation.dto.ReservationResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface ReservationService {

	ReservationResponseDto selectSeat(
		Long concertId,
		Long seatId);

	ReservationResponseDto completeReservation(
		Long reservationId);

	void cancelReservation(
		Long reservationId,
		String description);
}
