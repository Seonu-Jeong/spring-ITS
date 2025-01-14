package org.sparta.its.domain.cancelList.repository;

import org.sparta.its.domain.cancelList.entity.CancelList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancelListRepository extends JpaRepository<CancelList, Integer> {
}
