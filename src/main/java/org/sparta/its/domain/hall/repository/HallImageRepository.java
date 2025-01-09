package org.sparta.its.domain.hall.repository;

import org.sparta.its.domain.hall.entity.HallImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HallImageRepository extends JpaRepository<HallImage, Long> {

	// 쿼리 메소드

	// @Query 작성 메소드

	// Default 메소드
}
