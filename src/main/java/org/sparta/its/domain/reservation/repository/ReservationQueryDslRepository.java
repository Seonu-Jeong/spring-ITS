package org.sparta.its.domain.reservation.repository;

import java.time.LocalDate;

import org.sparta.its.domain.reservation.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservationQueryDslRepository {
	Page<Reservation> findAllReservations(
		LocalDate startAt,
		LocalDate endAt,
		String concertTitle,
		String singer,
		Pageable pageable);
}
