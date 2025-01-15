package org.sparta.its.domain.user.repository;

public interface UserQueryDslRepository {

	void updateUser(Long id, String email, String name, String phoneNumber, String password);

}