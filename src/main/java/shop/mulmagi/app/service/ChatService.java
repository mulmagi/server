package shop.mulmagi.app.service;

import java.util.List;

import shop.mulmagi.app.web.dto.ChatResponseDto;
import shop.mulmagi.app.web.dto.MessageDto;

public interface ChatService {
	List<ChatResponseDto.chatRoomDto> findAllChatRooms();
	List<MessageDto> getMessages(Long userId);
	void createRoom(Long userId);
	void deleteRoom(Long userId);
	void saveMessage(MessageDto messageDto);
}
