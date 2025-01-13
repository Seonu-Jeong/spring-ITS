package org.sparta.its.domain.concert.repository;

import org.sparta.its.domain.concert.dto.ConcertRequest;

public interface ConcertQueryDslRepository {

	void updateConcert(Long concertId, ConcertRequest.UpdateDto updateDto);
}
