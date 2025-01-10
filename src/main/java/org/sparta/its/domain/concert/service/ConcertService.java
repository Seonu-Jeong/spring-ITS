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
	public ConcertResponse.ReadDto getDetailConcert(Long concertId) {
		Concert concert = concertRepository.findByIdOrThrow(concertId);

		if (concert.getEndAt().isBefore(LocalDateTime.now())) {
			throw new ConcertException(ConcertErrorCode.ALREADY_ENDED);
		}

		return ConcertResponse.ReadDto.toDto(concert);
	}
}
