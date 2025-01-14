package org.sparta.its.domain.reservation.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.hall.entity.Seat;
import org.sparta.its.domain.reservation.entity.Reservation;
import org.sparta.its.domain.reservation.entity.ReservationStatus;
import org.sparta.its.global.exception.ReservationException;
import org.sparta.its.global.exception.errorcode.ReservationErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationQueryDslRepository {
	// 쿼리 메소드

	// @Query 작성 메소드

	//특정 자리 조회시 Pessimistic Lock적용
	//TODO: 동시성 제어해야함
	@Query("""
		SELECT r
		FROM reservation r
		WHERE r.seat = :seatId
		AND r.concert = :concertId
		AND r.concertDate = :concertDate
		AND r.status = :status
		""")
	Optional<Reservation> findReservationForSeatAndConcert(
		@Param("seatId") Seat seat,
		@Param("concertId") Concert concert,
		@Param("concertDate") LocalDate concertDate,
		@Param("status") ReservationStatus status
	);
	// Default 메소드
	default Reservation findByIdOrThrow(Long reservationId){
		return findById(reservationId).orElseThrow(() ->
			new ReservationException(ReservationErrorCode.NOT_FOUND_RESERVATION));
	}
}
