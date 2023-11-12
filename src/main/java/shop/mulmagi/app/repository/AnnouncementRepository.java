package shop.mulmagi.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.mulmagi.app.domain.Announcement;
import shop.mulmagi.app.domain.enums.AnnouncementCategory;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

	List<Announcement> findAll();
	List<Announcement> findByCategory(AnnouncementCategory category);

}
