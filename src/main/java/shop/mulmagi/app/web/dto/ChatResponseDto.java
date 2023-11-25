package shop.mulmagi.app.web.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ChatResponseDto {

	@Builder
	@Data
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class chatRoomDto{
		private Long userId;
		private MessageDto lastMessage;
	}
}
