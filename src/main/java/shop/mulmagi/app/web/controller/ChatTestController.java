package shop.mulmagi.app.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

//@Api(tags = "채팅 test API")
@Controller
@RequestMapping("/chat")
public class ChatTestController {

	//@ApiOperation(value = "채팅방 목록 test")
	@GetMapping("/list")
	public String chatList(Model model){
		return "chat/room";
	}

	//@ApiOperation(value = "채팅방 메시지 test")
	@GetMapping("/room/enter/{roomId}")
	public String chatList(Model model, @PathVariable String roomId){
		return "chat/roomdetail";
	}
}
