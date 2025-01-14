package org.sparta.its.domain.cancelList.service;

import java.util.List;

import org.sparta.its.domain.cancelList.dto.CancelListResponse;
import org.sparta.its.domain.cancelList.entity.CancelList;
import org.sparta.its.domain.cancelList.repository.CancelListRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CancelListService {

	private final CancelListRepository cancelListRepository;

	/**
	 * 취소 리스트 조회
	 *
	 * @param email 유저 이메일
	 * @param title 콘서트 이름
	 * @param orderBy 오름차순, 내림차순
	 * @param pageable 페이징
	 * @return {@link CancelListResponse.CancelListDtoRead} dto 응답
	 */
	@Transactional
	public List<CancelListResponse.CancelListDtoRead> getCancelLists(
		String email,
		String title,
		String orderBy,
		Pageable pageable) {

		Page<CancelList> cancelLists
			= cancelListRepository.findCancelLists(email, title, orderBy, pageable);

		return cancelLists.stream().map(CancelListResponse.CancelListDtoRead::toDto).toList();
	}

}
