package org.sparta.its.domain.hall.repository;

import org.sparta.its.domain.hall.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {

	// 쿼리 메소드

	// @Query 작성 메소드

	// Default 메소드
}
