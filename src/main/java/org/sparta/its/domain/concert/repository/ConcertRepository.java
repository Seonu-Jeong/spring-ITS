package org.sparta.its.domain.concert.repository;

import java.time.LocalDateTime;

import org.sparta.its.domain.concert.entity.Concert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

	// 쿼리 메소드

	// @Query 작성 메소드
	//TODO : order 고민
	@Query("SELECT c " +
		"FROM concert c " +
		"WHERE c.singer LIKE %:singer% OR c.title LIKE %:title% " +
		"ORDER BY c.startAt ASC")
	Page<Concert> findAllWithOrderBySingerAndTitle(
		@Param("singer") String singer,
		@Param("title") String title,
		@Param("startAt") LocalDateTime startAt,
		Pageable pageable);
	// Default 메소드
}
