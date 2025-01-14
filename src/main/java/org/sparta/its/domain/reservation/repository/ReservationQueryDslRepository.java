package org.sparta.its.domain.reservation.repository;

import java.time.LocalDateTime;

import org.sparta.its.domain.reservation.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservationQueryDslRepository {
	Page<Reservation> findAllReservations(
		LocalDateTime startAt,
		LocalDateTime endAt,
		String concertTitle,
		String singer,
		Pageable pageable);
}
