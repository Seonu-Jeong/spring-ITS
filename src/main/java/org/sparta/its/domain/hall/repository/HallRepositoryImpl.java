package org.sparta.its.domain.hall.repository;

import static org.sparta.its.domain.hall.entity.QHall.*;
import static org.sparta.its.domain.hall.entity.QHallImage.*;

import java.util.List;

import org.sparta.its.domain.hall.dto.HallResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class HallRepositoryImpl implements HallQueryDslRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public HallRepositoryImpl(EntityManager entityManager) {
		this.jpaQueryFactory = new JPAQueryFactory(entityManager);
	}

	/**
	 *
	 * @param name 공연장 이름
	 * @param location 공연장 위치
	 * @param pageable page = 1, 2, 3 ...번 페이지 번호, size = 페이지 마다 몇 개의 데이터
	 * @return
	 */
	@Override
	public Page<HallResponse.ReadDto> findByNameAndLocation(String name, String location, Pageable pageable) {
		List<HallResponse.ReadDto> fetch = jpaQueryFactory.select(
				Projections.constructor(HallResponse.ReadDto.class,
					hall.id,
					hall.name,
					hall.location,
					hall.capacity,
					hall.createdAt,
					hall.modifiedAt,
					hallImage.imageUrl,
					hall.isOpen))
			.from(hall)
			.leftJoin(hall.hallImages, hallImage)
			.where(hallNameLike(name), HallLocationLike(location))
			.groupBy(hall)
			.orderBy(hall.isOpen.desc(), hall.name.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> count = jpaQueryFactory.select(hall.count())
			.from(hall)
			.where(hallNameLike(name), HallLocationLike(location));

		return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
	}

	private BooleanExpression hallNameLike(String name) {
		if (name == null) {
			return null;
		}
		return hall.name.like("%" + name + "%");
	}

	private BooleanExpression HallLocationLike(String location) {
		if (location == null) {
			return null;
		}
		return hall.location.like("%" + location + "%");
	}

}
