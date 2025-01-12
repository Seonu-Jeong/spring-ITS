package org.sparta.its.domain.user.repository;

import static org.sparta.its.domain.user.entity.QUser.*;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import jakarta.persistence.EntityManager;

@Repository
public class UserQueryDslRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public UserQueryDslRepository(EntityManager entityManager) {
		this.jpaQueryFactory = new JPAQueryFactory(entityManager);
	}

	public Long updateUser(Long id, String email, String name, String phoneNumber, String password) {

		JPAUpdateClause clause = jpaQueryFactory.update(user);

		if (email != null) {
			clause.set(user.email, email);
		}

		if (name != null) {
			clause.set(user.name, name);
		}

		if (phoneNumber != null) {
			clause.set(user.phoneNumber, phoneNumber);
		}

		if (password != null) {
			clause.set(user.password, password);
		}

		clause.where(user.id.eq(id));

		return clause.execute();
	}
}