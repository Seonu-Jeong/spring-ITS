package org.sparta.its.domain.hall.repository;

import org.sparta.its.domain.hall.entity.Seat;
import org.sparta.its.global.exception.SeatException;
import org.sparta.its.global.exception.errorcode.SeatErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {

	// 쿼리 메소드

	// @Query 작성 메소드

	// Default 메소드
	default Seat findByIdOrThrow(Long seatId) {
		return findById(seatId)
			.orElseThrow(() -> new SeatException(SeatErrorCode.NOT_FOUND_SEAT));
	}
}
