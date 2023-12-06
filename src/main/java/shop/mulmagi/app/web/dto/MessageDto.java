package shop.mulmagi.app.web.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.mulmagi.app.domain.enums.MessageType;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Builder
public class MessageDto {

	private Long id;
	private Long userId;
	private String contents;
	private MessageType type;
	private Boolean isAdmin;
	private LocalDateTime createdAt;
}
