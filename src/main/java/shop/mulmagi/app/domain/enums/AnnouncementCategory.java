package shop.mulmagi.app.domain.enums;

public enum AnnouncementCategory {
	NORMAL("일반"),
	CHARGE("요금"),
	EVENT("이벤트"),
	SYSTEM("시스템"),
	ETC("기타");

	private final String categoryName;

	AnnouncementCategory(String categoryName) {this.categoryName = categoryName;}
}
