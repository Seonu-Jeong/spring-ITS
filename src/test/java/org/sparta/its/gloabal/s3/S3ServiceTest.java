package org.sparta.its.global.s3;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sparta.its.gloabal.s3.S3TestConfig;
import org.sparta.its.global.exception.ImageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;

@SpringBootTest
@Import(S3TestConfig.class) // Mock S3 설정 추가
@ExtendWith(MockitoExtension.class)
public class S3ServiceTest {
	@Autowired
	private S3Service s3Service;

	@MockitoBean
	private AmazonS3Client amazonS3;

	@Test
	@DisplayName("이미지 업로드 성공 테스트")
	void testUploadImages() throws IOException {
		// Given
		MockMultipartFile file = new MockMultipartFile(
			"image",
			"test.jpg",
			"image/jpeg",
			new byte[10] // 샘플 데이터
		);

		ImageFormat imageFormat = ImageFormat.CONCERT; // 예제 Enum 값
		Long domainId = 1L;

		Mockito.when(amazonS3.getResourceUrl(Mockito.anyString(), Mockito.anyString()))
			.thenReturn("https://mock-s3-url/test.jpg");

		// When
		List<String> uploadedUrls = s3Service.uploadImages(new MultipartFile[] {file}, imageFormat, domainId);

		// Then
		assertNotNull(uploadedUrls);
		assertEquals(1, uploadedUrls.size());
		assertEquals("https://mock-s3-url/test.jpg", uploadedUrls.get(0));
	}

	@Test
	@DisplayName("잘못된 파일 확장자 업로드 시 예외 발생")
	void testUploadInvalidExtension() {
		// Given
		MockMultipartFile file = new MockMultipartFile(
			"image",
			"test.txt",
			"text/plain",
			new byte[10]
		);

		ImageFormat imageFormat = ImageFormat.CONCERT;
		Long domainId = 1L;

		// When & Then
		assertThrows(ImageException.class, () ->
			s3Service.uploadImages(new MultipartFile[] {file}, imageFormat, domainId)
		);
	}

	// @Test
	// @DisplayName("S3 이미지 삭제 테스트")
	@Test
	void testDeleteImage() {
		// Given
		String imageUrl = "https://mock-s3-url/bucket/folder/test.jpg";
		ArgumentCaptor<String> objectKeyCaptor = ArgumentCaptor.forClass(String.class);

		Mockito.doNothing().when(amazonS3).deleteObject(Mockito.anyString(), objectKeyCaptor.capture());

		// When
		s3Service.deleteImprovementDelete(1L, ImageFormat.CONCERT);

		// Then
		List<String> capturedKeys = objectKeyCaptor.getAllValues();
		assertTrue(capturedKeys.stream().anyMatch(key -> key.contains("folder/test.jpg")));
	}

}
