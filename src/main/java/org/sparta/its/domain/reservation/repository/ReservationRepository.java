package org.sparta.its.domain.reservation.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.hall.entity.Seat;
import org.sparta.its.domain.reservation.entity.Reservation;
import org.sparta.its.domain.reservation.entity.ReservationStatus;
import org.sparta.its.global.exception.ReservationException;
import org.sparta.its.global.exception.errorcode.ReservationErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	// 쿼리 메소드

	// @Query 작성 메소드
	@Query("SELECT r FROM reservation r WHERE " +
		"(:startAt IS NULL OR r.concert.startAt = :startAt) AND " +
		"(:endAt IS NULL OR r.concert.endAt = :endAt) AND " +
		"(:title IS NULL OR r.concert.title LIKE %:title%) AND " +
		"(:singer IS NULL OR r.concert.singer LIKE %:singer%)")
	Page<Reservation> searchReservations(
		@Param("startAt") LocalDate startAt,
		@Param("endAt") LocalDate endAt,
		@Param("title") String title,
		@Param("singer") String singer,
		Pageable pageable
	);


	//특정 자리 조회시 Pessimistic Lock적용
	//TODO: 동시성 제어해야함
	@Query("""
		SELECT r
		FROM reservation r
		WHERE r.seat = :seatId
		AND r.concert = :concertId
		AND r.status = :status
		""")
	Optional<Reservation> findReservationForSeatAndConcert(
		@Param("seatId") Seat seat,
		@Param("concertId") Concert concert,
		@Param("status") ReservationStatus status
	);
	// Default 메소드
	default Reservation findByIdOrThrow(Long reservationId){
		return findById(reservationId).orElseThrow(() ->
			new ReservationException(ReservationErrorCode.NOT_FOUND_RESERVATION));
	}
}
