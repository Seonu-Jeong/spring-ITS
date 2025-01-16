package org.sparta.its.domain.cancelList.repository;

import static org.sparta.its.domain.cancelList.entity.QCancelList.*;

import java.util.List;

import org.sparta.its.domain.cancelList.entity.CancelList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class CancelListRepositoryImpl implements CancelListQueryDslRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public CancelListRepositoryImpl(EntityManager entityManager) {
		this.jpaQueryFactory = new JPAQueryFactory(entityManager);
	}

	/**
	 * 유저 이메일, 콘서트 이름, 오름차순 또는 내림차순 선택 가능
	 *
	 * @param email 유저 이메일
	 * @param title 콘서트 이름
	 * @param orderBy 정렬 방식
	 * @param pageable 페이징
	 * @return
	 */
	@Override
	public Page<CancelList> findCancelLists(
		String email,
		String title,
		String orderBy,
		Pageable pageable) {
		List<CancelList> fetch = jpaQueryFactory.selectFrom(cancelList)
			.where(
				correctEmail(email),
				titleLike(title)
			)
			.orderBy(orderSpecifier(orderBy))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> count = jpaQueryFactory.select(cancelList.count())
			.from(cancelList)
			.where(
				correctEmail(email),
				titleLike(title)
			);
		return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
	}

	private BooleanExpression correctEmail(String email) {
		if (email == null || email.isEmpty()) {
			return null;
		}
		return cancelList.user.email.eq(email);
	}

	private BooleanExpression titleLike(String title) {
		if (title == null) {
			return null;
		}
		return cancelList.concertTitle.like("%" + title + "%");
	}

	private OrderSpecifier<String> orderSpecifier(String orderBy) {
		return switch (orderBy.toUpperCase()) {
			case ORDER_DESC -> cancelList.concertTitle.desc();
			case ORDER_ASC -> cancelList.concertTitle.asc();
			default -> cancelList.concertTitle.asc();
		};
	}
}

