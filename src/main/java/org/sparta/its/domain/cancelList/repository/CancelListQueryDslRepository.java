package org.sparta.its.domain.cancelList.repository;

import org.sparta.its.domain.cancelList.entity.CancelList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CancelListQueryDslRepository {
	Page<CancelList> findCancelLists(
		String email,
		String title,
		String orderBy,
		Pageable pageable);
}
