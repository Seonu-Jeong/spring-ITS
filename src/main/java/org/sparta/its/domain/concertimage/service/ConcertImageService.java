package org.sparta.its.domain.concertimage.service;

import java.io.IOException;

import org.sparta.its.domain.concertimage.dto.ConcertImageRequest;
import org.sparta.its.domain.concertimage.dto.ConcertImageResponse;
import org.sparta.its.domain.concertimage.entity.ConcertImage;
import org.sparta.its.domain.concertimage.repository.ConcertImageRepository;
import org.sparta.its.global.exception.ImageException;
import org.sparta.its.global.exception.errorcode.ImageErrorCode;
import org.sparta.its.global.s3.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.SdkClientException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConcertImageService {

	private final ConcertImageRepository concertImageRepository;
	private final S3Service s3Service;

	@Transactional
	public ConcertImageResponse.UpdateDto updatedConcertImage(Long concertImageId,
		ConcertImageRequest.UpdateDto updateDto) {
		ConcertImage findConcertImage = concertImageRepository.findByIdOrThrow(concertImageId);

		String publicUrl;

		try {
			// 기존 이미지 삭제 및 새 이미지 업로드 후 url 받아옴
			publicUrl = s3Service.updateImage(
				updateDto.getImageFormat(),
				findConcertImage.getConcert().getId(),
				findConcertImage.getImageUrl(),
				updateDto.getImages());
		} catch (SdkClientException | IOException e) {
			throw new ImageException(ImageErrorCode.FILE_UPLOAD_FAILED);
		}

		// url 업데이트
		findConcertImage.updateImageUrl(publicUrl);

		return ConcertImageResponse.UpdateDto.toDto(findConcertImage);
	}
}
