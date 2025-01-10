package org.sparta.its.domain.hall.service;

import java.io.IOException;
import java.util.List;

import org.sparta.its.domain.hall.dto.HallRequest;
import org.sparta.its.domain.hall.dto.HallResponse;
import org.sparta.its.domain.hall.entity.Hall;
import org.sparta.its.domain.hall.entity.HallImage;
import org.sparta.its.domain.hall.entity.Seat;
import org.sparta.its.domain.hall.repository.HallImageRepository;
import org.sparta.its.domain.hall.repository.HallRepository;
import org.sparta.its.domain.hall.repository.SeatRepository;
import org.sparta.its.global.exception.ImageException;
import org.sparta.its.global.exception.errorcode.ImageErrorCode;
import org.sparta.its.global.s3.ImageFormat;
import org.sparta.its.global.s3.S3Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.amazonaws.SdkClientException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HallService {

	private final HallRepository hallRepository;
	private final HallImageRepository hallImageRepository;
	private final SeatRepository seatRepository;
	private final S3Service s3Service;

	// TODO 성능 개선 시, 사용
	// private final HallBulkRepository hallBulkRepository;

	/**
	 * 공연장을 저장 + 공연장 이미지 S3 업로드 + 업로드 URL 저장
	 * @param createDto 제목, 위치, 수용인원, 이미지
	 * @return {@link HallResponse.CreatDto} dto 응답
	 */
	@Transactional
	public HallResponse.CreatDto creatHall(HallRequest.CreateDto createDto) {

		if (hallRepository.existsByName(createDto.getName())) {
			throw new ImageException(ImageErrorCode.DUPLICATED_NAME);
		}

		Hall savedHall = hallRepository.save(createDto.toEntity());

		List<String> publicUrls;

		// TODO 좋지 않은 방법 예외 전환으로 생각해볼 것
		try {
			// 이미지 저장
			publicUrls
				= s3Service.uploadImages(
				createDto.getImages(), ImageFormat.HALL, savedHall.getId());
		} catch (SdkClientException | IOException e) {
			throw new ImageException(ImageErrorCode.FILE_UPLOAD_FAILED);
		}

		// 성능 낮음
		for (String publicUrl : publicUrls) {
			HallImage hallImage = new HallImage(savedHall, publicUrl);
			hallImageRepository.save(hallImage);
		}

		for (int i = 1; i <= savedHall.getCapacity(); i++) {
			Seat seat = new Seat(savedHall, i);
			seatRepository.save(seat);
		}

		// TODO 성능 개선 시, 사용
		// hallBulkRepository.saveAllHallImage(savedHall.getId(), publicUrls);
		// List<Integer> numberList = IntStream.rangeClosed(1, savedHall.getCapacity())
		// 	.boxed()
		// 	.toList();
		// hallBulkRepository.saveAllSeat(savedHall.getId(), numberList);

		return HallResponse.CreatDto.toDto(savedHall, publicUrls);
	}

	/**
	 * 동적 쿼리(이름, 위치에 따른) + 페이징을 통한 공연장 조회
	 * @param name 공연장 이름
	 * @param location 공연장 위치
	 * @param pageable 페이징
	 * @return  {@link HallResponse.ReadDto} dto 응답
	 */
	public List<HallResponse.ReadDto> getHalls(String name, String location, Pageable pageable) {
		Page<Hall> halls
			= hallRepository.findByNameAndLocation(name, location, pageable);

		return halls.stream().map(HallResponse.ReadDto::toDto).toList();
	}

	/**
	 * 공연장 세부 조회
	 * @param hallId 공연장 아이디
	 * @return {@link HallResponse.ReadDto} dto 응답
	 */
	public HallResponse.ReadDto getDetailHall(Long hallId) {
		Hall findHall = hallRepository.findByIdOrThrow(hallId);

		return HallResponse.ReadDto.toDto(findHall);
	}
}
