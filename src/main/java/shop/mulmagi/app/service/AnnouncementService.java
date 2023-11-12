package shop.mulmagi.app.service;

import java.util.List;

import shop.mulmagi.app.domain.enums.AnnouncementCategory;
import shop.mulmagi.app.web.dto.AnnouncementResponseDto;

public interface AnnouncementService {

	List<AnnouncementResponseDto.AnnouncementPageDto> getAnnouncements();
	List<AnnouncementResponseDto.AnnouncementPageDto> getAnnouncementCategory(AnnouncementCategory category);
	AnnouncementResponseDto.AnnouncementDetailDto getAnnouncementDetail(Long id);
	void upload(String title, AnnouncementCategory category, String content, String imgUrl, String fileUrl);
	void deleteAnnouncement(Long id);
	void update(String title, AnnouncementCategory category, String content, String imgUrl, String fileUrl);
}
