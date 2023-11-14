package shop.mulmagi.app.web.dto;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import shop.mulmagi.app.domain.enums.AnnouncementCategory;

public class AnnouncementRequestDto {

	@Getter @Setter
	public static class UploadDto{
		@ApiModelProperty(example = "이용수칙 개정 공지사항")
		@ApiParam(name = "title", value = "공지사항 제목 입력")
		private String title;

		@ApiModelProperty(example = "NORMAL")
		@ApiParam(name = "category", value = "공지사항 카테고리 입력")
		private AnnouncementCategory category;

		@ApiModelProperty(example = "이용수칙 개정 안내드립니다")
		@ApiParam(name = "content", value = "공지사항 내용 입력")
		private String content;

		@ApiModelProperty(example = "a.png")
		@ApiParam(name = "photo", value = "이미지 파일 선택")
		private MultipartFile photo;

		@ApiModelProperty(example = "b.txt")
		@ApiParam(name = "file", value = "파일 선택")
		private MultipartFile file_0;
	}
	@Getter @Setter
	public static class UpdateDto{
		@ApiModelProperty(example = "이용수칙 개정 공지사항")
		@ApiParam(name = "title", value = "공지사항 제목 입력")
		private String title;

		@ApiModelProperty(example = "NORMAL")
		@ApiParam(name = "category", value = "공지사항 카테고리 입력")
		private AnnouncementCategory category;

		@ApiModelProperty(example = "이용수칙 개정 안내드립니다")
		@ApiParam(name = "content", value = "공지사항 내용 입력")
		private String content;

		@ApiModelProperty(example = "a.png")
		@ApiParam(name = "photo", value = "이미지 파일 선택")
		private MultipartFile photo;

		@ApiModelProperty(example = "b.txt")
		@ApiParam(name = "file", value = "파일 선택")
		private MultipartFile file_0;
	}
}
