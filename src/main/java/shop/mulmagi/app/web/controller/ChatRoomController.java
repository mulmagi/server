package shop.mulmagi.app.web.controller;

import java.util.List;

import io.swagger.annotations.ApiImplicitParams;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.exception.CustomExceptions;
import shop.mulmagi.app.exception.ResponseMessage;
import shop.mulmagi.app.exception.StatusCode;
import shop.mulmagi.app.service.ChatService;
import shop.mulmagi.app.service.UserService;
import shop.mulmagi.app.web.controller.base.BaseController;
import shop.mulmagi.app.web.dto.ChatResponseDto;
import shop.mulmagi.app.web.dto.MessageDto;
import shop.mulmagi.app.web.dto.base.DefaultRes;

@Api(tags = "채팅 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController extends BaseController {

	private final ChatService chatService;
	private final UserService userService;

	@ApiOperation(value = "모든 채팅방 불러오기 API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
	})
	@GetMapping("/rooms")
	public ResponseEntity getAllChatRooms(){
		try {
			logger.info("Received request: method={}, path={}, description={}", "Get", "/chat/rooms", "모든 채팅방 불러오기 API");
			User user = userService.getCurrentUser();
			List<ChatResponseDto.chatRoomDto> res = chatService.findAllChatRooms();

			return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.CHATROOM_READ_SUCCESS, res), HttpStatus.OK);
		} catch (CustomExceptions.Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "특정 채팅방 메시지 불러오기 API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
	})
	@ApiImplicitParam(name = "userId", value = "채팅방(에 속한 사용자) ID", example = "1")
	@GetMapping("/room/{userId}")
	public ResponseEntity getChatMessages(@PathVariable Long userId){
		try {
			logger.info("Received request: method={}, path={}, description={}", "Get", "/chat/room/{userId}", "특정 채팅방 메시지 불러오기 API");
			User user = userService.getCurrentUser();
			List<MessageDto> res = chatService.getMessages(user.getId()); //userId == roomId

			return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.MESSAGE_READ_SUCCESS, res), HttpStatus.OK);
		} catch (CustomExceptions.Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "채팅방 생성 API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
	})
	@ApiImplicitParam(name = "userId", value = "문의를 시작할 사용자 ID", example = "1")
	@PostMapping("/room/{userId}")
	public ResponseEntity createChatRoom(@PathVariable Long userId){
		try {
			logger.info("Received request: method={}, path={}, description={}", "Post", "/chat/room/{userId}", "채팅방 생성 API");
			User user = userService.getCurrentUser();
			chatService.createRoom(user.getId());

			return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.CHATROOM_CREAT_SUCCESS), HttpStatus.OK);
		} catch (CustomExceptions.Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "채팅방 삭제 API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
	})
	@ApiImplicitParam(name = "userId", value = "문의를 끝낼 사용자 ID", example = "1")
	@DeleteMapping("/room/{userId}")
	public ResponseEntity deleteChatRoom(@PathVariable Long userId){
		try {
			logger.info("Received request: method={}, path={}, description={}", "Delete", "/chat/room/{userId}", "채팅방 삭제 API");
			User user = userService.getCurrentUser();
			chatService.deleteRoom(user.getId());

			return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.CHATROOM_DELETE_SUCCESS), HttpStatus.OK);
		} catch (CustomExceptions.Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}
}
