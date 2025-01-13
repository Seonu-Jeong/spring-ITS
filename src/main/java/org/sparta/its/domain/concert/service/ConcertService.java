package org.sparta.its.domain.concert.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.sparta.its.domain.concert.dto.ConcertRequest;
import org.sparta.its.domain.concert.dto.ConcertResponse;
import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.concert.repository.ConcertRepository;
import org.sparta.its.domain.concert.util.ConcertValidator;
import org.sparta.its.domain.concertimage.entity.ConcertImage;
import org.sparta.its.domain.concertimage.repository.ConcertImageRepository;
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

		// 콘서트 시작 시간 죵료 시간 비교 예외처리
		ConcertValidator.validateCrossTimes(createDto);

		// 콘서트 시작 날짜 죵료 날짜 비교 예외처리
		ConcertValidator.validateCrossDates(createDto);

		// 콘서트 시작 날짜 및 졸료 날짜 현재 시점 기준 예외처리
		ConcertValidator.isBeforeToday(createDto);

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
	 * @param pageable {@link Pageable} 페이지 번호와 사이즈 결정
	 * @return {@link ConcertResponse.ReadDto}  응답 Dto
	 */
	@Transactional(readOnly = true)
	public List<ConcertResponse.ReadDto> getConcerts(String singer, String title, String order, Pageable pageable) {
		// 정렬 변수 설정
		Sort sort;

		switch (order) {
			case "오름차순" -> sort = Sort.by(Sort.Order.desc("startAt"));
			case "내림차순" -> sort = Sort.by(Sort.Order.asc("startAt"));
			default -> throw new ConcertException(ConcertErrorCode.INCORRECT_VALUE);
		}

		LocalDateTime today = LocalDateTime.now();
		Pageable SortByStartAt = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

		Page<Concert> allConcerts = concertRepository.findAllWithOrderBySingerAndTitleAndToday(singer, title, today,
			SortByStartAt);

		return allConcerts.stream().map(ConcertResponse.ReadDto::toDto).toList();
	}

	/**
	 * 콘서트 상세 조회
	 * @param concertId 콘서트 고유 식별자
	 * @return {@link ConcertResponse.ReadDto} 형태로 응답
	 */
	@Transactional(readOnly = true)
	public ConcertResponse.ReadDto getDetailConcert(Long concertId) {
		Concert concert = concertRepository.findByIdOrThrow(concertId);

		if (concert.getEndAt().isBefore(LocalDateTime.now())) {
			throw new ConcertException(ConcertErrorCode.ALREADY_ENDED);
		}

		return ConcertResponse.ReadDto.toDto(concert);
	}

	/**
	 * 콘서트 정보 수정
	 * @param concertId 콘서트 고유 식별자
	 * @param updateDto 수정 요청 Dto
	 * @return {@link ConcertResponse.UpdateDto} 형태로 응답
	 */
	@Transactional
	public ConcertResponse.UpdateDto updatedConcert(Long concertId, ConcertRequest.UpdateDto updateDto) {
		// id 로 콘서트 정보 불러옴
		Concert concert = concertRepository.findByIdOrThrow(concertId);

		// Querydsl 로 수정된 concert 정보를 받아옴
		concertRepository.updateConcert(concertId, updateDto);

		Concert updatedConcert = concertRepository.findByIdOrThrow(concertId);

		// 기존 콘서트 시작(종료) 날짜와 요청값 콘서트 시작(종료) 날짜 비교 예외처리
		ConcertValidator.validateCrossDates(updateDto, concert);

		// 기존 콘서트 시작(종료) 시간과 콘서트 종료 시작(종료) 시간 비교 예외처리
		ConcertValidator.validateCrossTimes(updateDto, concert);

		// 콘서트 시작 시간과 콘서트 종료 시간 비교 예외처리
		ConcertValidator.validateRunningTime(updateDto);

		// 콘서트 시작 날짜와 콘서트 죵료 날짜 비교 예외처리
		ConcertValidator.validateStartAndEndDates(updateDto);

		// 콘서트 시작 날짜 및 졸료 날짜 현재 시점 기준 예외처리
		ConcertValidator.isBeforeToday(updateDto);

		return ConcertResponse.UpdateDto.toDto(updatedConcert);
	}
}
