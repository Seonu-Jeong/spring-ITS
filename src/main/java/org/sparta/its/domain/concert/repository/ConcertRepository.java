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
	@Query("""
		SELECT c
			FROM concert c
			WHERE c.singer LIKE %:singer%
			AND c.title LIKE %:title%
			AND c.endAt >:today
		""")
	Page<Concert> findAllWithOrderBySingerAndTitle(
		@Param("singer") String singer,
		@Param("title") String title,
		@Param("today") LocalDateTime today,
		Pageable pageable);
	// Default 메소드
}
