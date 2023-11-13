package shop.mulmagi.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import shop.mulmagi.app.domain.Announcement;
import shop.mulmagi.app.domain.enums.AnnouncementCategory;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

	List<Announcement> findAll();
	List<Announcement> findByCategory(AnnouncementCategory category);

	@Modifying
	@Transactional
	@Query("UPDATE Announcement a SET a.title = :title, a.category = :category, a.content = :content, a.imgUrl = :imgUrl, a.fileUrl = :fileUrl WHERE a.id = :id")
	void updateAnnouncement(@Param("id") Long id, @Param("title")String title, @Param("category")AnnouncementCategory category, @Param("content")String content, @Param("imgUrl")String imgUrl, @Param("fileUrl")String fileUrl);
}
