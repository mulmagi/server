package shop.mulmagi.app.web.controller;

import static shop.mulmagi.app.domain.enums.MessageType.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import shop.mulmagi.app.exception.CustomExceptions;
import shop.mulmagi.app.exception.ResponseMessage;
import shop.mulmagi.app.exception.StatusCode;
import shop.mulmagi.app.service.ChatService;
import shop.mulmagi.app.service.S3UploadService;
import shop.mulmagi.app.web.controller.base.BaseController;
import shop.mulmagi.app.web.dto.MessageRequestDto;
import shop.mulmagi.app.web.dto.MessageResponseDto;
import shop.mulmagi.app.web.dto.base.DefaultRes;

@Api(tags = "메시지 관련 API")
@RestController
@RequiredArgsConstructor
public class MessageController extends BaseController {
	private final SimpMessageSendingOperations sendingOperations;
	private final ChatService chatService;
	private final S3UploadService s3UploadService;

	@MessageMapping("/chat/message") //stomp
	public void sendTextMessage(MessageRequestDto.TextMessageDto messageDto) {
		//logger.info(messageDto.getContents());
		MessageResponseDto.MessageDto messageRes = chatService.getMessage(messageDto);
		if(messageRes.getType().equals(ENTER) && !messageRes.getIsAdmin()){
			messageRes.setContents(messageRes.getUserId().toString() + "님이 입장하였습니다");
		}
		messageRes = chatService.saveMessage(messageRes); //id와 createdAt 가져옴
		sendingOperations.convertAndSend("/topic/chat/room/" + messageRes.getUserId().toString(), messageRes);
	}

	@ApiOperation(value = "POST로 채팅보내기 test API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
	})
	@PostMapping("/chat/message/test")
	public ResponseEntity sendTextMessageTest(@ModelAttribute MessageRequestDto.TextMessageDto request) {
		try {
			//logger.info(messageDto.getContents());
			MessageResponseDto.MessageDto messageRes = chatService.getMessage(request);
			if (messageRes.getType().equals(ENTER) && !messageRes.getIsAdmin()) {
				messageRes.setContents(messageRes.getUserId().toString() + "님이 입장하였습니다");
			}
			messageRes = chatService.saveMessage(messageRes); //id와 createdAt 가져옴
			sendingOperations.convertAndSend("/topic/chat/room/" + messageRes.getUserId().toString(), messageRes);
			return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.POST_MESSAGE_SUCCESS, messageRes), HttpStatus.OK);
		} catch (CustomExceptions.Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}

	//이미지 채팅 전송
	@ApiOperation(value = "이미지 메시지 전송하기 API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
	})
	@PostMapping("/chat/message")
	public ResponseEntity sendImgMessage(@ModelAttribute MessageRequestDto.ImgMessageDto request) {
		try {
			MultipartFile img = request.getImg();
			String imgUrl = "";
			if (img != null && !img.isEmpty()) {
				imgUrl = s3UploadService.uploadAWSChatImg(img);
			}
			MessageResponseDto.MessageDto messageRes = chatService.getMessage(request, imgUrl);

			messageRes = chatService.saveMessage(messageRes);
			sendingOperations.convertAndSend("/topic/chat/room/" + messageRes.getUserId().toString(), messageRes);
			return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.POST_IMAGE_SUCCESS, messageRes), HttpStatus.OK);
		}
		catch (CustomExceptions.Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}
}
