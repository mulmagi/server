package shop.mulmagi.app.web.dto;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import shop.mulmagi.app.domain.enums.MessageType;

public class MessageRequestDto {

	@Getter @Setter
	public static class TextMessageDto{
		@ApiModelProperty(example = "2")
		@ApiParam(name = "userId", value = "유저(채팅방) id 입력")
		private Long userId;
		
		@ApiModelProperty(example = "문의드립니다")
		@ApiParam(name = "contents", value = "메시지 내용 입력")
		private String contents;

		@ApiModelProperty(example = "TEXT")
		@ApiParam(name = "type", value = "메시지 타입 입력")
		private MessageType type;

		@ApiModelProperty(example = "false")
		@ApiParam(name = "isAdmin", value = "관리자 여부 입력")
		private Boolean isAdmin;
	}
	@Getter @Setter
	public static class ImgMessageDto{
		@ApiModelProperty(example = "2")
		@ApiParam(name = "userId", value = "유저(채팅방) id 입력")
		private Long userId;

		@ApiModelProperty(example = "a.jpg")
		@ApiParam(name = "img", value = "이미지 파일 입력")
		private MultipartFile img;

		@ApiModelProperty(example = "IMAGE")
		@ApiParam(name = "type", value = "메시지 타입 입력")
		private MessageType type;

		@ApiModelProperty(example = "false")
		@ApiParam(name = "isAdmin", value = "관리자 여부 입력")
		private Boolean isAdmin;
	}
}
