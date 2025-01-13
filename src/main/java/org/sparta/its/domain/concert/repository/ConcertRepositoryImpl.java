package org.sparta.its.domain.concert.repository;

import static org.sparta.its.domain.concert.entity.QConcert.*;

import org.sparta.its.domain.concert.dto.ConcertRequest;
import org.sparta.its.domain.concert.entity.Concert;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import jakarta.persistence.EntityManager;

@Repository
public class ConcertRepositoryImpl implements ConcertQuerydslRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public ConcertRepositoryImpl(EntityManager entityManager) {
		this.jpaQueryFactory = new JPAQueryFactory(entityManager);
	}

	// TODO : 성능 개선 JPA 메서드 find 해보기 (영속성 컨텍스트)
	@Override
	public Concert updateQuery(Long concertId, ConcertRequest.UpdateDto updateDto) {

		JPAUpdateClause where = jpaQueryFactory
			.update(concert)
			.where(concert.id.eq(concertId));

		if (updateDto.getTitle() != null) {
			where = where.set(concert.title, updateDto.getTitle());
		}

		if (updateDto.getStartAt() != null) {
			where = where.set(concert.startAt, updateDto.getStartAt());
		}

		if (updateDto.getEndAt() != null) {
			where = where.set(concert.endAt, updateDto.getEndAt());
		}

		if (updateDto.getRunningStartTime() != null) {
			where = where.set(concert.runningStartTime, updateDto.getRunningStartTime());
		}

		if (updateDto.getRunningEndTime() != null) {
			where = where.set(concert.runningEndTime, updateDto.getRunningEndTime());
		}

		where.execute();

		Concert updatedConcert = jpaQueryFactory.selectFrom(concert)
			.where(concert.id.eq(concertId))
			.fetchOne();

		return updatedConcert;
	}
}
