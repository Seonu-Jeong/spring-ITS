package org.sparta.its.domain.concertimage.repository;

import org.sparta.its.domain.concertimage.entity.ConcertImage;
import org.sparta.its.global.exception.ConcertImageException;
import org.sparta.its.global.exception.errorcode.ConcertImageErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertImageRepository extends JpaRepository<ConcertImage, Long> {

	// 쿼리 메소드

	// @Query 작성 메소드

	// Default 메소드
	default ConcertImage findByIdOrThrow(Long concertImageId) {
		return findById(concertImageId).orElseThrow(() ->
			new ConcertImageException(ConcertImageErrorCode.NOT_FOUND));
	}

}
