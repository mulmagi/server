package shop.mulmagi.app.web.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import shop.mulmagi.app.domain.enums.MessageType;

public class MessageRequestDto {

	@Getter
	public static class TextMessageDto{
		private Long id;
		private Long userId;
		private String contents;
		private MessageType type;
		private boolean isAdmin;
	}
	@Getter
	public static class ImgMessageDto{
		private Long id;
		private Long userId;
		private MultipartFile img;
		private MessageType type;
		private String imgUrl;
		private boolean isAdmin;
	}
}
