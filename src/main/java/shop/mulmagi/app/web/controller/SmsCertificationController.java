package shop.mulmagi.app.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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


@RestController
@RequiredArgsConstructor
public class SmsCertificationController extends BaseController {
    private final UserService userService;
    @Value("coolsms.apiKey")
    private String apiKey;
    @Value("coolsms.apiSecret")
    private String apiSecret;

    @PostMapping("/sms-certification/sends")
    public ResponseEntity sendSms(@RequestBody UserDto.SmsCertificationRequest requestDto) {
        try {
            userService.sendSms(requestDto.getPhone());
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.SMS_CERT_MESSAGE_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    //인증번호 확인
    @PostMapping("/sms-certification/confirms")
    public ResponseEntity<Void> SmsVerification(@RequestBody UserDto.SmsCertificationRequest requestDto) {
        userService.verifySms(requestDto);
        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.SMS_CERT_SUCCESS), HttpStatus.OK);
    }


}
