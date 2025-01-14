package org.sparta.its.domain.concert.repository;

import java.time.LocalDateTime;

import org.sparta.its.domain.concert.dto.ConcertRequest;
import org.sparta.its.domain.concert.entity.Concert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConcertQueryDslRepository {

	void updateConcert(Long concertId, ConcertRequest.UpdateDto updateDto);

	Page<Concert> findStatisticsWithOrderByTitleAndStartAtAndEndAt(String title, LocalDateTime startAt,
		LocalDateTime endAt, String order, Pageable pageable);

}
