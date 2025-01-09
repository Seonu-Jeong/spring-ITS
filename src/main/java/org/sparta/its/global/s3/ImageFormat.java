package org.sparta.its.global.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageFormat {
	CONCERT("/CONCERT", new String[] {".jpg", ".png"}),
	HALL("/HALL", new String[] {".jpg", ".png"});

	private final String path;
	private final String[] whiteList;
}
