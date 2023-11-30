package shop.mulmagi.app.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shop.mulmagi.app.exception.CustomExceptions;
import shop.mulmagi.app.exception.ResponseMessage;
import shop.mulmagi.app.exception.StatusCode;
import shop.mulmagi.app.service.UserService;
import shop.mulmagi.app.web.controller.base.BaseController;
import shop.mulmagi.app.web.dto.UserDto;
import shop.mulmagi.app.web.dto.base.DefaultRes;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController extends BaseController {
     private final UserService userService;

    @PostMapping("/sms-certification/send")
    public ResponseEntity<?> sendSms(@RequestBody UserDto.SmsCertificationRequest requestDto) throws Exception {
        try {
            userService.sendSms(requestDto);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.SMS_CERT_MESSAGE_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    //인증번호 확인
    @PostMapping("/sms-certification/confirm")
    public ResponseEntity<Void> SmsVerification(@RequestBody UserDto.SmsCertificationRequest requestDto) throws Exception{
        try {
            userService.verifySms(requestDto);
            log.info(ResponseMessage.SMS_CERT_SUCCESS);
            userService.registerMember(requestDto);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.USER_REGISTER_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }
}
