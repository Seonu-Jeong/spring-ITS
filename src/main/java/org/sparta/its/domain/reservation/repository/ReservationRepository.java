package org.sparta.its.domain.reservation.repository;

import java.util.Optional;

import org.sparta.its.domain.reservation.entity.Reservation;
import org.sparta.its.domain.reservation.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	//특정 자리 조회시 Pessimistic Lock적용
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT r FROM reservation r WHERE r.seat.seat_id = :seatId AND r.concert.concert_id = :concertId AND r.status = :status")
	Optional<Reservation> findReservationForSeatAndConcert(
		@Param("seatId") Long seatId,
		@Param("concertId") Long concertId,
		@Param("status") ReservationStatus status
	);
}
