package shop.mulmagi.app.converter;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import shop.mulmagi.app.domain.Message;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.web.dto.ChatResponseDto;
import shop.mulmagi.app.web.dto.MessageDto;
import shop.mulmagi.app.web.dto.MessageRequestDto;
import shop.mulmagi.app.web.dto.MessageResponseDto;

@Component
@RequiredArgsConstructor
public class ChatConverter {

	public MessageDto toMessageDto(Message message){
		return MessageDto.builder()
			.id(message.getId())
			.userId(message.getUser().getId())
			.contents(message.getContent())
			.type(message.getType())
			.isAdmin(message.isAdmin())
			.build();
	}

	public ChatResponseDto.chatRoomDto toChatRoomDto(User user, Message message){
		return ChatResponseDto.chatRoomDto.builder()
			.userId(user.getId())
			.lastMessage(toMessageDto(message))
			.build();
	}

	public Message toMessage(User user, MessageResponseDto.MessageDto messageDto){
		return Message.builder()
			.user(user)
			.type(messageDto.getType())
			.content(messageDto.getContents())
			.isAdmin(messageDto.getIsAdmin())
			.build();
	}

	public MessageResponseDto.MessageDto toResponseMessage(MessageRequestDto.TextMessageDto messageDto){
		return MessageResponseDto.MessageDto.builder()
			.userId(messageDto.getUserId())
			.contents(messageDto.getContents())
			.type(messageDto.getType())
			.isAdmin(messageDto.getIsAdmin())
			.build();
	}

	public MessageResponseDto.MessageDto toResponseMessage(MessageRequestDto.ImgMessageDto messageDto, String imgUrl){
		return MessageResponseDto.MessageDto.builder()
			.userId(messageDto.getUserId())
			.contents(imgUrl)
			.type(messageDto.getType())
			.isAdmin(messageDto.getIsAdmin())
			.build();
	}
}
