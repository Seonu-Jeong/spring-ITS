package org.sparta.its.domain.hall.repository;

import org.sparta.its.domain.hall.dto.HallResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HallQueryDslRepository {
	Page<HallResponse.ReadDto> findByNameAndLocation(String name, String location, Pageable pageable);
}
