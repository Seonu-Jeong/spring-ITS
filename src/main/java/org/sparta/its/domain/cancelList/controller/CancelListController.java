package org.sparta.its.domain.cancelList.controller;

import java.util.List;

import org.sparta.its.domain.cancelList.dto.CancelListResponse;
import org.sparta.its.domain.cancelList.service.CancelListService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CancelListController {

	private final CancelListService cancelListService;

	/**
	 * 취소 리스트 조회
	 *
	 * @param email {@link RequestParam} 유저 이메일
	 * @param title {@link RequestParam} 콘서트 이름
	 * @param orderBy {@link RequestParam} 오름차순, 내림차순
	 * @param pageable {@link PageableDefault} 페이징
	 * @return {@link ResponseEntity} httpStatus 와 {@link CancelListResponse.CancelListDtoRead} dto 응답
	 */
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/cancelLists")
	public ResponseEntity<List<CancelListResponse.CancelListDtoRead>> getCancelLists(
		@RequestParam(required = false) String email,
		@RequestParam(required = false) String title,
		@RequestParam(defaultValue = "ASC") String orderBy,
		@PageableDefault(value = 5) Pageable pageable) {

		List<CancelListResponse.CancelListDtoRead> cancelLists = cancelListService.getCancelLists(email, title, orderBy, pageable);

		return ResponseEntity.status(HttpStatus.OK).body(cancelLists);
	}
}
