package org.sparta.its.global.s3;

import static org.sparta.its.global.exception.errorcode.ImageErrorCode.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sparta.its.global.exception.ImageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.RequiredArgsConstructor;

/**
 * create on 2025. 01. 08.
 * create by IntelliJ IDEA.
 *
 * S3 관련 Service.
 *
 * @author TaeHyeon Kim
 */
@Service
@RequiredArgsConstructor
public class S3Service {

	private final AmazonS3Client amazonS3;

	private static final String TOP_LEVEL_DOMAIN_WITH_DOT_AND_SLASH = ".com/";

	@Value("${spring.cloud.aws.s3.bucket}")
	private String BUCKET;

	/**
	 * 이미지 파일 전처리
	 *
	 * @param images 이미지파일
	 * @param imageFormat 이미지 관련 포맷팅
	 * @param domainId 이미지 저장을 원하는 도메인 고유 식별자
	 * @return {@link List<String>} S3에 저장된 이미지 URL 리스트
	 * @throws IOException S3 관련 IO 예외
	 */
	public List<String> uploadImages(
		MultipartFile[] images,
		ImageFormat imageFormat,
		Long domainId) throws IOException {

		// 이미지가 빈값인지 확인
		long count = Arrays.stream(images).filter(MultipartFile::isEmpty).count();
		if (count > 0) {
			throw new ImageException(BAD_IMAGE_FILE);
		}

		List<String> uploadedUrls = new ArrayList<>();
		for (MultipartFile image : images) {
			String fileUrl = saveFileToS3(image, imageFormat, domainId);
			uploadedUrls.add(fileUrl);
		}
		return uploadedUrls;
	}

	/**
	 * S3 이미지를 업데이트
	 *
	 * @param imageFormat 이미지 관련 포맷팅
	 * @param id 이미지 수정을 원하는 도메인 고유 식별자
	 * @param imageUrl 이미지 수정을 원하는 이미지 URL
	 * @param images 이미지파일
	 * @return {@link String} S3에 저장된 이미지 URL
	 * @throws IOException S3 관련 IO 예외
	 */
	public String updateImage(
		ImageFormat imageFormat,
		Long id,
		String imageUrl,
		MultipartFile[] images) throws IOException {

		// 이미지파일이 1개 일때만 업데이트 가능
		if (images.length != 1) {
			throw new ImageException(BAD_IMAGE_FILE);
		}

		// 기존 이미지 삭제
		String objectKey = getObjectKey(imageUrl);
		amazonS3.deleteObject(BUCKET, objectKey);

		// 이미지를 새로 업로드하고 url 을 받아옴
		return saveFileToS3(images[0], imageFormat, id);
	}

	/**
	 * aws s3 이미지 삭제하는 함수
	 *
	 * @param imageUrls 공연장 이미지 urlList
	 */
	public void deleteImages(
		List<String> imageUrls) {

		for (String imageUrl : imageUrls) {
			String objectKey = getObjectKey(imageUrl);
			amazonS3.deleteObject(BUCKET, objectKey);
		}

	}

	/**
	 * url 을 통해 aws-s3에 오브젝트 경로를 찾는 함수
	 *
	 * @param imageUrl 이미지 url
	 * @return {@link String} S3 에 필요한 오브젝트 키
	 */
	private String getObjectKey(String imageUrl) {
		// ".com/" 이후의 위치를 찾음
		int objectKeyStartIndex = imageUrl.indexOf(TOP_LEVEL_DOMAIN_WITH_DOT_AND_SLASH) + 5; // ".com/" 다음 위치
		String objectKeyEncoded = imageUrl.substring(objectKeyStartIndex);

		// 객체 키 디코딩
		return URLDecoder.decode(objectKeyEncoded, StandardCharsets.UTF_8);
	}

	/**
	 * 실질적으로 s3에 저장하는 함수
	 *
	 * @param image 이미지파일
	 * @param imageFormat 이미지 관련 포맷팅
	 * @param id 이미지 수정을 원하는 도메인 고유 식별자
	 * @return {@link String} S3에 저장된 이미지 URL
	 * @throws IOException
	 */
	private String saveFileToS3(MultipartFile image, ImageFormat imageFormat, Long id) throws IOException {

		// 파일 확장자 가져오기
		String fileExtension = getFileExtensionWithDot(image.getOriginalFilename());

		// 확장자 유효성 검사
		validateFileExtension(fileExtension, imageFormat);

		// 기본 버킷 설정
		StringBuilder packageName = new StringBuilder(BUCKET);

		// imageFormat 에 정의된 경로에 따라 설정
		StringBuilder secondPackageName = new StringBuilder(imageFormat.getPath());

		// 해당 도메인 id에 따른 패키지 추가
		secondPackageName.append("/").append(id);

		// 이미지 이름과 확장자를 가져옴
		String s3FileName = image.getOriginalFilename();

		// 최종 패키지
		packageName.append(secondPackageName);

		// s3 저장
		ObjectMetadata objMeta = new ObjectMetadata();
		objMeta.setContentLength(image.getInputStream().available());
		amazonS3.putObject(packageName.toString(), s3FileName, image.getInputStream(), objMeta);

		// substring 은 '/' 해주기 위함
		return amazonS3.getResourceUrl(BUCKET, secondPackageName.substring(1) + "/" + s3FileName);
	}

	/**
	 * 확장자가 있는지 확인하기 위함
	 *
	 * @param originalFileName 이미지 파일의 이름과 파일 확장자
	 * @return 파일 확장자
	 */
	private String getFileExtensionWithDot(String originalFileName) {
		int lastIndexOfDot = originalFileName.lastIndexOf('.');

		if (lastIndexOfDot == -1) {
			throw new ImageException(NO_EXTENSION_FILE);
		}

		return originalFileName.substring(lastIndexOfDot);
	}

	/**
	 * 허용된 확장자인지 확인하기 위함
	 *
	 * @param fileExtension 파일 확장자
	 * @param imageFormat 이미지 관련 포맷팅
	 */
	private void validateFileExtension(String fileExtension, ImageFormat imageFormat) {
		if (!Arrays.asList(imageFormat.getWhiteList()).contains(fileExtension)) {
			throw new ImageException(NOT_ALLOW_FILE_EXTENSION);
		}
	}
}
