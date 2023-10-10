package shop.mulmagi.app.domain.enums;

public enum MessageType {
	SATISFACTION("만족"),
	DISSATISFACTION("불만족");

	private final String typeName;

	MessageType(String typeName) {this.typeName = typeName;}
}
