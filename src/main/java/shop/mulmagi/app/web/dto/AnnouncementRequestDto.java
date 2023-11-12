package shop.mulmagi.app.web.dto;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import shop.mulmagi.app.domain.enums.AnnouncementCategory;

public class AnnouncementRequestDto {

	@Getter @Setter
	public static class UploadDto{
		private String title;
		private AnnouncementCategory category;
		private String content;
		private MultipartFile photo;
		private MultipartFile file_0;
	}
	@Getter @Setter
	public static class UpdateDto{
		private String title;
		private AnnouncementCategory category;
		private String content;
		private MultipartFile photo;
		private MultipartFile file_0;
	}
}
