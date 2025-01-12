package org.sparta.its.domain.hall.repository;

import org.sparta.its.domain.hall.entity.HallImage;
import org.sparta.its.global.exception.HallImageException;
import org.sparta.its.global.exception.errorcode.HallImageErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HallImageRepository extends JpaRepository<HallImage, Long> {

	// 쿼리 메소드

	// @Query 작성 메소드

	// Default 메소드
	default HallImage findByIdOrThrow(Long id) {
		return findById(id)
			.orElseThrow(() -> new HallImageException(HallImageErrorCode.NOT_FOUND_HALL_IMAGE));
	}
}
