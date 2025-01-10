package org.sparta.its.domain.concert.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.sparta.its.domain.concert.dto.ConcertRequest;
import org.sparta.its.domain.concert.dto.ConcertResponse;
import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.concert.entity.ConcertImage;
import org.sparta.its.domain.concert.repository.ConcertImageRepository;
import org.sparta.its.domain.concert.repository.ConcertRepository;
import org.sparta.its.domain.hall.entity.Hall;
import org.sparta.its.domain.hall.repository.HallRepository;
import org.sparta.its.global.exception.ConcertException;
import org.sparta.its.global.exception.ImageException;
import org.sparta.its.global.exception.errorcode.ConcertErrorCode;
import org.sparta.its.global.exception.errorcode.ImageErrorCode;
import org.sparta.its.global.s3.ImageFormat;
import org.sparta.its.global.s3.S3Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.SdkClientException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConcertService {

	private final ConcertRepository concertRepository;
	private final ConcertImageRepository concertImageRepository;
	private final HallRepository hallRepository;
	private final S3Service s3Service;

	/**
	 * 콘서트 등록 및 콘서트 이미지 생성
	 * @param createDto 요청 값
	 * @return {@link ConcertResponse.CreateDto} 반환 값
	 */
	@Transactional
	public ConcertResponse.CreateDto createConcert(ConcertRequest.CreateDto createDto) {

		Hall findHall = hallRepository.findByIdOrThrow(createDto.getHallId());

		Concert saveConcert = concertRepository.save(createDto.toEntity(findHall));

		List<String> publicUrls;

		try {
			publicUrls = s3Service.uploadImages(createDto.getImages(), ImageFormat.CONCERT, saveConcert.getId());
		} catch (SdkClientException | IOException e) {
			throw new ImageException(ImageErrorCode.FILE_UPLOAD_FAILED);
		}

		for (String publicUrl : publicUrls) {
			ConcertImage concertImage = new ConcertImage(saveConcert, publicUrl);
			concertImageRepository.save(concertImage);
		}

		return ConcertResponse.CreateDto.toDto(saveConcert, publicUrls);
	}

	/**
	 * 콘서트 가수명 및 콘서트명으로 다건 조회
	 * @param singer 가수명 검색 조건
	 * @param title  콘서트명 검색 조건
	 * @param order {@link Sort} 오름차순과 내림차순 결정
	 * @param page {@link Pageable} 페이지 번호 입력
	 * @param size {@link Pageable} 페이지 갯수 입력
	 * @return {@link ConcertResponse.FindDto}  응답 Dto
	 */
	@Transactional(readOnly = true)
	public List<ConcertResponse.FindDto> findAll(String singer, String title, String order, Integer page,
		Integer size) {

		// 정렬 변수 설정
		Sort sort;

		switch (order) {
			case "오름차순" -> sort = Sort.by(Sort.Order.desc("startAt"));
			case "내림차순" -> sort = Sort.by(Sort.Order.asc("startAt"));
			default -> throw new ConcertException(ConcertErrorCode.INCORRECT_VALUE);
		}

		Pageable pageable = PageRequest.of(page, size, sort);

		LocalDateTime today = LocalDateTime.now();

		Page<Concert> allConcerts = concertRepository.findAllWithOrderBySingerAndTitleAndToday(singer, title, today,
			pageable);

		return allConcerts.stream().map(ConcertResponse.FindDto::toDto).toList();
	}

	// public ConcertResponse.FindDto findConcert(Long concertId) {
	//
	// }
}
