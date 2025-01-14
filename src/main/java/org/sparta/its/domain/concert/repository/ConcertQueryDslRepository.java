package org.sparta.its.domain.concert.repository;

import java.time.LocalDate;

import org.sparta.its.domain.concert.dto.ConcertRequest;
import org.sparta.its.domain.concert.entity.Concert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConcertQueryDslRepository {

	void updateConcert(Long concertId, ConcertRequest.UpdateDto updateDto);

	Page<Concert> findStatisticsWithOrderByTitleAndStartAtAndEndAt(String title, LocalDate startAt,
		LocalDate endAt, String order, Pageable pageable);

}
