package shop.mulmagi.app.web.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shop.mulmagi.app.domain.enums.AnnouncementCategory;

public class AnnouncementResponseDto {

	@Builder @Data
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class AnnouncementPageDto{
		private Long id;
		private AnnouncementCategory category;
		private String title;
		private LocalDateTime createdAt;
	}

	@Builder @Data
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class AnnouncementDetailDto{
		private Long id;
		private AnnouncementCategory category;
		private String title;
		private String content;
		private LocalDateTime createdAt;
		private String imgUrl;
		private String fileUrl;
	}
}
