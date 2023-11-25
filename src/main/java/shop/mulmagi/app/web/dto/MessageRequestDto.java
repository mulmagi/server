package shop.mulmagi.app.web.dto;

import lombok.Getter;
import shop.mulmagi.app.domain.enums.MessageType;

public class MessageRequestDto {

	@Getter
	public static class MessageDto{
		private Long id;
		private Long userId;
		private String contents;
		private MessageType type;
		private String imgName;
		private String imgCode; //base64 encoding
		private boolean isAdmin;
	}
}
