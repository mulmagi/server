package shop.mulmagi.app.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import shop.mulmagi.app.domain.Message;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.exception.ResponseMessage;
import shop.mulmagi.app.exception.StatusCode;
import shop.mulmagi.app.repository.UserRepository;
import shop.mulmagi.app.service.impl.ChatServiceImpl;
import shop.mulmagi.app.web.controller.base.BaseController;
import shop.mulmagi.app.web.dto.ChatResponseDto;
import shop.mulmagi.app.web.dto.MessageDto;
import shop.mulmagi.app.web.dto.base.DefaultRes;

@Api(tags = "채팅 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController extends BaseController {

	private final UserRepository userRepository;
	private final ChatServiceImpl chatService;

	@ApiOperation(value = "모든 채팅방 불러오기 API")
	@GetMapping("/rooms")
	public ResponseEntity getAllChatRooms(){
		try {
			List<ChatResponseDto.chatRoomDto> res = chatService.findAllChatRooms();

			return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.CHATROOM_READ_SUCCESS, res), HttpStatus.OK);
		} catch (Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "특정 채팅방 메시지 불러오기 API")
	@GetMapping("/room/{userId}")
	public ResponseEntity getChatMessages(@PathVariable Long userId){
		try {
			logger.info("Received request: method={}, path={}, description={}", "Get", "/chat/room/{userId}", "특정 채팅방 메시지 불러오기 API");

			List<MessageDto> res = chatService.getMessages(userId); //userId == roomId

			return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.MESSAGE_READ_SUCCESS, res), HttpStatus.OK);
		} catch (Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "채팅방 생성 API")
	@PostMapping("room/{userId}")
	public ResponseEntity createChatRoom(@PathVariable Long userId){
		try {
			chatService.createRoom(userId);

			return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.CHATROOM_CREAT_SUCCESS), HttpStatus.OK);
		} catch (Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "채팅방 삭제 API")
	@DeleteMapping("room/{userId}")
	public ResponseEntity deleteChatRoom(@PathVariable Long userId){
		try {
			chatService.deleteRoom(userId);

			return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.CHATROOM_DELETE_SUCCESS), HttpStatus.OK);
		} catch (Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}
}
