package shop.mulmagi.app.web.dto;

import java.util.List;

import org.springframework.stereotype.Service;
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
		@ApiParam(name = "umbrellaStandId", value = "Umbrella Stand ID 입력", required = true)
		private String title;

		@ApiModelProperty(example = "NORMAL")
		private AnnouncementCategory category;

		@ApiModelProperty(example = "이용수칙 개정 안내드립니다")
		private String content;

		@ApiModelProperty(example = "a.png")
		private MultipartFile photo;

		@ApiModelProperty(example = "b.txt")
		private MultipartFile file_0;
	}
	@Getter @Setter
	public static class UpdateDto{
		@ApiModelProperty(example = "이용수칙 개정 공지사항")
		private String title;

		@ApiModelProperty(example = "NORMAL")
		private AnnouncementCategory category;

		@ApiModelProperty(example = "이용수칙 개정 안내드립니다")
		private String content;

		@ApiModelProperty(example = "a.png")
		private MultipartFile photo;

		@ApiModelProperty(example = "b.txt")
		private MultipartFile file_0;
	}
}
