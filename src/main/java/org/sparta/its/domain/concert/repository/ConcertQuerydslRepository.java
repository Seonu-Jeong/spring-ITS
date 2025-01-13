package org.sparta.its.domain.concert.repository;

import org.sparta.its.domain.concert.dto.ConcertRequest;
import org.sparta.its.domain.concert.entity.Concert;

public interface ConcertQuerydslRepository {

	Concert updateQuery(Long concertId, ConcertRequest.UpdateDto updateDto);
}
