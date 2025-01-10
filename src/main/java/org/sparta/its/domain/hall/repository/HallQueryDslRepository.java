package org.sparta.its.domain.hall.repository;

import org.sparta.its.domain.hall.entity.Hall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HallQueryDslRepository {
	Page<Hall> findByNameAndLocation(String name, String location, Pageable pageable);
}
