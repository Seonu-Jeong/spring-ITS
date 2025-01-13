package org.sparta.its.domain.concert.repository;

import static org.sparta.its.domain.concert.entity.QConcert.*;

import org.sparta.its.domain.concert.dto.ConcertRequest;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import jakarta.persistence.EntityManager;

@Repository
public class ConcertRepositoryImpl implements ConcertQueryDslRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final EntityManager entityManager;

	public ConcertRepositoryImpl(EntityManager entityManager) {
		this.jpaQueryFactory = new JPAQueryFactory(entityManager);
		this.entityManager = entityManager;
	}

	// TODO : 성능 개선 JPA 메서드 find 해보기 (영속성 컨텍스트)
	@Override
	public void updateConcert(Long concertId, ConcertRequest.UpdateDto updateDto) {

		JPAUpdateClause query = jpaQueryFactory
			.update(concert)
			.where(concert.id.eq(concertId));

		if (updateDto.getTitle() != null) {
			query.set(concert.title, updateDto.getTitle());
		}

		if (updateDto.getStartAt() != null) {
			query.set(concert.startAt, updateDto.getStartAt());
		}

		if (updateDto.getEndAt() != null) {
			query.set(concert.endAt, updateDto.getEndAt());
		}

		if (updateDto.getRunningStartTime() != null) {
			query.set(concert.runningStartTime, updateDto.getRunningStartTime());
		}

		if (updateDto.getRunningEndTime() != null) {
			query.set(concert.runningEndTime, updateDto.getRunningEndTime());
		}

		query.execute();

		entityManager.flush();
		entityManager.clear();
	}
}
