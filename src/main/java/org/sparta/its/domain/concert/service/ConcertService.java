package org.sparta.its.domain.concert.service;

import java.io.IOException;
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

		if (createDto.getRunningStartTime().isAfter(createDto.getRunningEndTime())) {
			throw new ConcertException(ConcertErrorCode.IS_NOT_AFTER_TIME);
		}

		if (createDto.getStartAt().isAfter(createDto.getEndAt())) {
			throw new ConcertException(ConcertErrorCode.IS_NOT_AFTER_DATE);
		}

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
}
