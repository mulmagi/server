package shop.mulmagi.app.web.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shop.mulmagi.app.domain.enums.MessageType;

public class MessageResponseDto {

	@Builder
	@Data
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class MessageDto{
		private Long id;
		private Long userId;
		private String contents;
		private MessageType type;
		private boolean isAdmin;
	}
}
