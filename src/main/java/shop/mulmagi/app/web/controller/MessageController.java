package shop.mulmagi.app.web.controller;

import static shop.mulmagi.app.domain.enums.MessageType.*;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import shop.mulmagi.app.service.impl.ChatServiceImpl;
import shop.mulmagi.app.service.impl.S3UploadServiceImpl;
import shop.mulmagi.app.web.controller.base.BaseController;
import shop.mulmagi.app.web.dto.MessageRequestDto;
import shop.mulmagi.app.web.dto.MessageResponseDto;

@Api(tags = "메시지 관련 API")
@RestController
@RequiredArgsConstructor
public class MessageController extends BaseController {
	private final SimpMessageSendingOperations sendingOperations;
	private final ChatServiceImpl chatService;
	private final S3UploadServiceImpl s3UploadService;

	@MessageMapping("/chat/enter")
	public void enter(MessageRequestDto.TextMessageDto messageDto){
		MessageResponseDto.MessageDto messageRes = chatService.getMessage(messageDto);
		if(!messageRes.getIsAdmin()){
			messageRes.setContents("환영합니다 "+ messageRes.getUserId().toString() + "님");
			messageRes.setType(ENTER);
			sendingOperations.convertAndSend("/topic/chat/room/" + messageRes.getUserId().toString(), messageRes);

			chatService.saveMessage(messageRes);
		}
	}

	@MessageMapping("/chat/message")
	public void sendTextMessage(MessageRequestDto.TextMessageDto messageDto) {
		logger.info(messageDto.getContents());
		MessageResponseDto.MessageDto messageRes = chatService.getMessage(messageDto);
		sendingOperations.convertAndSend("/topic/chat/room/" + messageRes.getUserId().toString(), messageRes);

		chatService.saveMessage(messageRes);
	}
	//이미지 채팅 전송
	@PostMapping("/chat/message")
	public void sendImgMessage(@RequestBody MessageRequestDto.ImgMessageDto request) {
		MultipartFile img = request.getImg();
		String imgUrl = "";
		if(img != null && !img.isEmpty()) {
			imgUrl = s3UploadService.uploadAWSChatImg(img);
		}
		MessageResponseDto.MessageDto messageRes = chatService.getMessage(request, imgUrl);
		sendingOperations.convertAndSend("/topic/chat/room/" + messageRes.getUserId().toString(), messageRes);

		chatService.saveMessage(messageRes);
	}
}
