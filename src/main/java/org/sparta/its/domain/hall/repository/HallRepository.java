package org.sparta.its.domain.hall.repository;

import org.sparta.its.domain.hall.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HallRepository extends JpaRepository<Hall, Long> {

	// 쿼리 메소드
	boolean existsByName(String name);

	// @Query 작성 메소드

	// Default 메소드
}