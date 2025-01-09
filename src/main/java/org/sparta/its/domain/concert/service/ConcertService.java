package org.sparta.its.domain.concert.service;

import java.util.List;

import org.sparta.its.domain.concert.dto.ConcertRequest;
import org.sparta.its.domain.concert.dto.ConcertResponse;
import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.concert.fake.HallRepository;
import org.sparta.its.domain.concert.repository.ConcertRepository;
import org.sparta.its.domain.hall.entity.Hall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConcertService {

	private final ConcertRepository concertRepository;
	private final HallRepository hallRepository;

	@Transactional
	public ConcertResponse.ResponseDto createConcert(ConcertRequest.CreateDto requestDto, List<MultipartFile> images) {

		Hall findHall = hallRepository.findByIdOrThrow(requestDto.getHallId());

		Concert concert = requestDto.toEntity(findHall, images);

		concertRepository.save(concert);

		return ConcertResponse.ResponseDto.toDto(concert);
	}
}
