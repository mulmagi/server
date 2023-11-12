package shop.mulmagi.app.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;

import lombok.RequiredArgsConstructor;
import shop.mulmagi.app.converter.AnnouncementConverter;
import shop.mulmagi.app.domain.Announcement;
import shop.mulmagi.app.domain.enums.AnnouncementCategory;
import shop.mulmagi.app.repository.AnnouncementRepository;
import shop.mulmagi.app.service.AnnouncementService;
import shop.mulmagi.app.service.S3UploadService;
import shop.mulmagi.app.web.dto.AnnouncementResponseDto;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {
	private final AnnouncementRepository announcementRepository;
	private final AnnouncementConverter announcementConverter;
	private final S3UploadServiceImpl s3UploadService;

	//전체 공지사항 조회
	@Override
	public List<AnnouncementResponseDto.AnnouncementPageDto> getAnnouncements(){
		List<Announcement> announcementList = announcementRepository.findAll();

		List<AnnouncementResponseDto.AnnouncementPageDto> announcements = announcementList.stream().map(
			announcement -> announcementConverter.toAnnouncmentPage(announcement)
		).collect(Collectors.toList());

		return announcements;
	}

	//특정 카테고리 공지사항 조회
	@Override
	public List<AnnouncementResponseDto.AnnouncementPageDto> getAnnouncementCategory(AnnouncementCategory category){
		List<Announcement> announcementList = announcementRepository.findByCategory(category);

		List<AnnouncementResponseDto.AnnouncementPageDto> announcements = announcementList.stream().map(
			announcement -> announcementConverter.toAnnouncmentPage(announcement)
		).collect(Collectors.toList());

		return announcements;
	}
	
	//공지사항 세부내용 조회
	@Override
	public AnnouncementResponseDto.AnnouncementDetailDto getAnnouncementDetail(Long id){
		Announcement announcement = announcementRepository.findById(id).orElseThrow();
		AnnouncementResponseDto.AnnouncementDetailDto announcementDetail = announcementConverter.toAnnouncementDetail(announcement);

		return announcementDetail;
	}

	@Override
	public void upload(String title, AnnouncementCategory category, String content, String imgUrl, String fileUrl){
		Announcement announcement = announcementConverter.toAnnouncement(title, category, content, imgUrl, fileUrl);
		announcementRepository.save(announcement);
	}

	@Override
	public void deleteAnnouncement(Long id){
		Announcement announcement = announcementRepository.findById(id).orElseThrow();
		s3UploadService.deleteAWSFile(announcement);
		announcementRepository.delete(announcement);
	}

	@Override
	public void update(String title, AnnouncementCategory category, String content, String imgUrl, String fileUrl){
		Announcement announcement = announcementConverter.toAnnouncement(title, category, content, imgUrl, fileUrl);
		announcementRepository.save(announcement);
	}
}
