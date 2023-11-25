package shop.mulmagi.app.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import shop.mulmagi.app.domain.enums.MessageType;

@Getter @Setter
@Builder
public class MessageDto {

	private Long id;
	private Long userId;
	private String contents;
	private MessageType type;
	private boolean isAdmin;
}
