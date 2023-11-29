package shop.mulmagi.app.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mulmagi.app.converter.ChatConverter;
import shop.mulmagi.app.domain.Message;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.repository.MessageRepository;
import shop.mulmagi.app.repository.UserRepository;
import shop.mulmagi.app.service.ChatService;
import shop.mulmagi.app.web.dto.ChatResponseDto;
import shop.mulmagi.app.web.dto.MessageDto;
import shop.mulmagi.app.web.dto.MessageRequestDto;
import shop.mulmagi.app.web.dto.MessageResponseDto;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

	private final MessageRepository messageRepository;
	private final UserRepository userRepository;
	private final ChatConverter chatConverter;

	@Override
	public List<ChatResponseDto.chatRoomDto> findAllChatRooms(){
		List<User> complainingUsers = userRepository.findByIsComplaining(true); //활성화된 roomId들은 현재 문의중인 user의 id와 동일

		List<ChatResponseDto.chatRoomDto> chatRoomDtos = complainingUsers.stream().map(
			User -> chatConverter.toChatRoomDto(User, messageRepository.findTopByUserOrderByCreatedAtDesc(User))
		).collect(Collectors.toList());

		return chatRoomDtos;
	}

	@Cacheable(key = "#userId", value = "ChatRoom", cacheManager = "redisCacheManager")
	@Override
	public List<MessageDto> getMessages(Long userId){
		User user = userRepository.findById(userId).orElseThrow();
		List<Message> messages = messageRepository.findByUser(user);

		List<MessageDto> messageDtos = messages.stream().map(
			message -> chatConverter.toMessageDto(message)
		).collect(Collectors.toList());

		return messageDtos;
	}

	@Override
	public void createRoom(Long userId){
		User user = userRepository.findById(userId).orElseThrow();
		user.startComplain(); //문의중인 상태로 변경
		userRepository.save(user);
	}

	@Override
	public void deleteRoom(Long userId){
		User user = userRepository.findById(userId).orElseThrow();
		user.endComplain(); //문의중이 아닌 상태로 변경
		userRepository.save(user);
	}

	@CacheEvict(key = "#messageDto.userId", value = "ChatRoom", cacheManager = "redisCacheManager")
	@Override
	public void saveMessage(MessageResponseDto.MessageDto messageDto){
		User user = userRepository.findById(messageDto.getUserId()).orElseThrow();
		Message message = chatConverter.toMessage(user, messageDto);
		messageRepository.save(message);
	}

	public MessageResponseDto.MessageDto getMessage(MessageRequestDto.TextMessageDto request){
		return chatConverter.toResponseMessage(request);
	}

	public MessageResponseDto.MessageDto getMessage(MessageRequestDto.ImgMessageDto request, String imgUrl){
		return chatConverter.toResponseMessage(request, imgUrl);
	}
}
