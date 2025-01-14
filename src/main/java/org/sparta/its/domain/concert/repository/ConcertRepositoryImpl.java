package org.sparta.its.domain.concert.repository;

import static org.sparta.its.domain.concert.entity.QConcert.*;

import java.time.LocalDateTime;
import java.util.List;

import org.sparta.its.domain.concert.dto.ConcertRequest;
import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.hall.entity.QHall;
import org.sparta.its.domain.reservation.entity.QReservation;
import org.sparta.its.global.exception.ConcertException;
import org.sparta.its.global.exception.errorcode.ConcertErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import jakarta.persistence.EntityManager;

@Repository
public class ConcertRepositoryImpl implements ConcertQueryDslRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final EntityManager entityManager;
	private final PageableHandlerMethodArgumentResolverCustomizer pageableCustomizer;

	public ConcertRepositoryImpl(EntityManager entityManager,
		PageableHandlerMethodArgumentResolverCustomizer pageableCustomizer) {
		this.jpaQueryFactory = new JPAQueryFactory(entityManager);
		this.entityManager = entityManager;
		this.pageableCustomizer = pageableCustomizer;
	}

	// TODO : 성능 개선 JPA 메서드 find 해보기 (영속성 컨텍스트)

	/**
	 * 콘서트 정보 수정
	 * @param concertId 콘서트 고유 식별자
	 * @param updateDto 수정 요청 Dto
	 */
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

	@Override
	public Page<Concert> findStatisticsWithOrderByTitleAndStartAtAndEndAt(String title, LocalDateTime startAt,
		LocalDateTime endAt, String order, Pageable pageable) {
		QHall hall = QHall.hall;
		QReservation reservation = QReservation.reservation;
		List<Concert> fetch = jpaQueryFactory
			.select(concert)
			.from(concert)
			.where(concertTitleLike(title)
				.and(isAfterStartAt(startAt))
				.and(isBeforeEndAt(endAt)))
			.leftJoin(concert.hall, hall)
			.fetchJoin()
			.leftJoin(concert.reservations, reservation)
			.fetchJoin()
			.orderBy(decideOrderBy(order))
			.offset(pageable.getPageNumber())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> count = jpaQueryFactory
			.select(concert.count())
			.from(concert)
			.where(
				concertTitleLike(title)
					.and(isAfterStartAt(startAt))
					.and(isBeforeEndAt(endAt)));

		return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
	}

	private BooleanExpression concertTitleLike(String title) {
		if (title == null) {
			return null;
		}
		return concert.title.like("%" + title + "%");
	}

	private BooleanExpression isAfterStartAt(LocalDateTime startAt) {
		if (startAt == null) {
			return null;
		}
		return concert.startAt.goe(startAt);
	}

	private BooleanExpression isBeforeEndAt(LocalDateTime endAt) {
		if (endAt == null) {
			return null;
		}
		return concert.endAt.loe(endAt);
	}

	private OrderSpecifier<LocalDateTime> decideOrderBy(String order) {
		return switch (order) {
			case "오름차순" -> concert.startAt.desc();
			case "내림차순" -> concert.startAt.asc();
			default -> throw new ConcertException(ConcertErrorCode.INCORRECT_VALUE);
		};
	}

}
