package org.sparta.its.domain.cancelList.repository;

import org.sparta.its.domain.cancelList.entity.CancelList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancelListRepository extends JpaRepository<CancelList, Integer>, CancelListQueryDslRepository{
	// 쿼리 메소드

	// @Query 작성 메소드

	// Default 메소드
}
