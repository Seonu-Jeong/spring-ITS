package org.sparta.its.domain.hall.repository;

import org.sparta.its.domain.hall.entity.Hall;
import org.sparta.its.global.exception.HallException;
import org.sparta.its.global.exception.errorcode.HallErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HallRepository extends JpaRepository<Hall, Long>, HallQueryDslRepository {

	// 쿼리 메소드
	boolean existsByName(String name);

	Hall findHallByIdAndIsOpen(Long hallId, boolean trueStatus);

	// @Query 작성 메소드

	// Default 메소드
	default Hall findByIdOrThrow(Long hallId) {
		return findById(hallId)
			.orElseThrow(() -> new HallException(HallErrorCode.NOT_FOUND_HALL));
	}

}
