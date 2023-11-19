package shop.mulmagi.app.converter;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import shop.mulmagi.app.domain.Announcement;
import shop.mulmagi.app.domain.enums.AnnouncementCategory;
import shop.mulmagi.app.web.dto.AnnouncementResponseDto;

@Component
@RequiredArgsConstructor
public class AnnouncementConverter {

	public AnnouncementResponseDto.AnnouncementPageDto toAnnouncmentPage(Announcement announcement){
		return AnnouncementResponseDto.AnnouncementPageDto.builder()
			.id(announcement.getId())
			.category(announcement.getCategory())
			.title(announcement.getTitle())
			.createdAt(announcement.getCreatedAt())
			.build();
	}
	public AnnouncementResponseDto.AnnouncementDetailDto toAnnouncementDetail(Announcement announcement){
		return AnnouncementResponseDto.AnnouncementDetailDto.builder()
			.id(announcement.getId())
			.category(announcement.getCategory())
			.title(announcement.getTitle())
			.content(announcement.getContent())
			.createdAt(announcement.getCreatedAt())
			.imgUrl(announcement.getImgUrl())
			.fileUrl(announcement.getFileUrl())
			.build();
	}

	public Announcement toAnnouncement(String title, AnnouncementCategory category, String content, String imgUrl, String fileUrl){
		return Announcement.builder()
			.title(title)
			.category(category)
			.content(content)
			.imgUrl(imgUrl)
			.fileUrl(fileUrl)
			.build();
	}
}
