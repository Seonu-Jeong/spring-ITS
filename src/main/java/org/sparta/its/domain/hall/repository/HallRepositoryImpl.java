package org.sparta.its.domain.hall.repository;

import static org.sparta.its.domain.hall.entity.QHall.*;

import java.util.List;

import org.sparta.its.domain.hall.dto.HallRequest;
import org.sparta.its.domain.hall.entity.Hall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import jakarta.persistence.EntityManager;

@Repository
public class HallRepositoryImpl implements HallQueryDslRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final EntityManager entityManager;

	public HallRepositoryImpl(EntityManager entityManager) {
		this.jpaQueryFactory = new JPAQueryFactory(entityManager);
		this.entityManager = entityManager;
	}

	/**
	 * TODO	N+1 문제 해결해야함
	 * @param name 공연장 이름
	 * @param location 공연장 위치
	 * @param pageable page = 1, 2, 3 ...번 페이지 번호, size = 페이지 마다 몇 개의 데이터
	 * @return
	 */
	@Override
	public Page<Hall> findByNameAndLocation(String name, String location, Pageable pageable) {
		List<Hall> fetch = jpaQueryFactory.selectFrom(hall)
			.where(hallNameLike(name), hallLocationLike(location))
			.orderBy(hall.isOpen.desc(), hall.name.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> count = jpaQueryFactory.select(hall.count())
			.from(hall)
			.where(hallNameLike(name), hallLocationLike(location));

		return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
	}

	/**
	 * 공연장 수정하는 동적 쿼리
	 * @param hallId 공연장 고유 식별자
	 * @param updateDto 이름, 위치
	 */
	@Override
	public void updateHall(Long hallId, HallRequest.UpdateDto updateDto) {

		JPAUpdateClause query = jpaQueryFactory.update(hall).where(hall.id.eq(hallId));

		if (updateDto.getName() != null) {
			query.set(hall.name, updateDto.getName());
		}

		if (updateDto.getLocation() != null) {
			query.set(hall.location, updateDto.getLocation());
		}

		query.execute();

		entityManager.flush();
		entityManager.clear();
	}

	private BooleanExpression hallNameLike(String name) {
		if (name == null) {
			return null;
		}
		return hall.name.like("%" + name + "%");
	}

	private BooleanExpression hallLocationLike(String location) {
		if (location == null) {
			return null;
		}
		return hall.location.like("%" + location + "%");
	}

}
