package shop.mulmagi.app.web.dto;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

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
		private Boolean isAdmin;
		@JsonSerialize(using = LocalDateTimeSerializer.class)
		@JsonDeserialize(using = LocalDateTimeDeserializer.class)
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
		private LocalDateTime createdAt;
	}
}
