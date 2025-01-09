package org.sparta.its.domain.hall.repository;

import org.sparta.its.domain.hall.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
