package shop.mulmagi.app.web.controller;

import static shop.mulmagi.app.domain.enums.MessageType.*;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import shop.mulmagi.app.service.impl.ChatServiceImpl;
import shop.mulmagi.app.web.controller.base.BaseController;
import shop.mulmagi.app.web.dto.MessageDto;
import shop.mulmagi.app.web.dto.MessageRequestDto;

@Api(tags = "메시지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class MessageController extends BaseController {
	private final SimpMessageSendingOperations sendingOperations;
	private final ChatServiceImpl chatService;

	@MessageMapping("/enter")
	public void enter(MessageDto message){
		if(!message.isAdmin()){
			message.setContents("환영합니다 "+ message.getUserId().toString() + "님");
		}
		message.setType(ENTER);
		sendingOperations.convertAndSend("/topic/chat/room/" + message.getUserId().toString(), message);

		chatService.saveMessage(message);
	}

	@MessageMapping("/message")
	public void sendMessage(MessageDto message) {
		/*if(message.getType() == IMAGE){
			base64 img code를 multipartfile로 변환
			s3에 업로드하고 url반환
		}*/
		sendingOperations.convertAndSend("/topic/chat/room/" + message.getUserId().toString(), message);

		chatService.saveMessage(message);
	}
}
