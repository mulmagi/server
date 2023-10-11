package shop.mulmagi.app.domain.enums;

public enum MessageType {
	TEXT("텍스트"),
	IMAGE("이미지"),
	ETC("기타");

	private final String typeName;

	MessageType(String typeName) {this.typeName = typeName;}
}
