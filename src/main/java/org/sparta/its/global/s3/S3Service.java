package org.sparta.its.global.s3;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sparta.its.global.exception.ImageException;
import org.sparta.its.global.exception.errorcode.ImageErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final AmazonS3Client amazonS3;

	@Value("${spring.cloud.aws.s3.bucket}")
	private String BUCKET;

	/**
	 * aws s3에 올리기 위한 함수
	 * @param images
	 * @param imageFormat
	 * @param domainId
	 * @return
	 * @throws IOException
	 */
	public List<String> uploadImages(
		MultipartFile[] images,
		ImageFormat imageFormat,
		Long domainId) throws IOException {

		// 이미지가 빈값인지 확인
		long count = Arrays.stream(images).filter(MultipartFile::isEmpty).count();
		if (count > 0) {
			throw new ImageException(ImageErrorCode.BAD_IMAGE_FILE);
		}

		List<String> uploadedUrls = new ArrayList<>();
		for (MultipartFile image : images) {
			String fileUrl = saveFileToS3(image, imageFormat, domainId);
			uploadedUrls.add(fileUrl);
		}
		return uploadedUrls;
	}

	public void deleteImages(
		List<String> imageUrls) {

		for (String imageUrl : imageUrls) {
			String objectKey = getObjectKey(imageUrl);
			amazonS3.deleteObject(BUCKET, objectKey);
		}

	}

	private String getObjectKey(String imageUrl) {
		// ".com/" 이후의 위치를 찾음
		int objectKeyStartIndex = imageUrl.indexOf(".com/") + 5; // ".com/" 다음 위치
		String objectKeyEncoded = imageUrl.substring(objectKeyStartIndex);

		// 객체 키 디코딩
		return URLDecoder.decode(objectKeyEncoded, StandardCharsets.UTF_8);
	}

	/**
	 * 실질적으로 s3에 저장하는 함수
	 * @param image
	 * @param imageFormat
	 * @param id
	 * @return
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
	 * @param originalFileName
	 * @return
	 */
	private String getFileExtensionWithDot(String originalFileName) {
		int lastIndexOfDot = originalFileName.lastIndexOf('.');

		if (lastIndexOfDot == -1) {
			throw new ImageException(ImageErrorCode.NO_EXTENSION_FILE);
		}

		return originalFileName.substring(lastIndexOfDot);
	}

	/**
	 * 허용된 확장자인지 확인하기 위함
	 * @param fileExtension
	 * @param imageFormat
	 */
	private void validateFileExtension(String fileExtension, ImageFormat imageFormat) {
		if (isWhiteList(fileExtension, imageFormat.getWhiteList())) {
			throw new ImageException(ImageErrorCode.NOT_ALLOW_FILE_EXTENSION);
		}
	}

	// PatternMatchUtils 으로 enum 의 whiteList 에 명시된 것 만 허용
	private boolean isWhiteList(String fileExtension, String[] whiteList) {
		return !PatternMatchUtils.simpleMatch(whiteList, fileExtension);
	}
}
