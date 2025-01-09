package org.sparta.its.domain.concert.repository;

import org.sparta.its.domain.concert.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

	// 쿼리 메소드

	// @Query 작성 메소드

	// Default 메소드
}
