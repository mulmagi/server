package shop.mulmagi.app.web.controller;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import net.nurigo.sdk.NurigoApp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shop.mulmagi.app.exception.ResponseMessage;
import shop.mulmagi.app.exception.StatusCode;
import shop.mulmagi.app.service.SmsCertifiactionService;
import shop.mulmagi.app.web.dto.UserDto;
import shop.mulmagi.app.web.dto.base.DefaultRes;


@RestController
public class SmsCertificationController {
    final DefaultMessageService smsCertificationService;
    @Value("coolsms.apiKey")
    private String apiKey;
    @Value("coolsms.apiSecret")
    private String apiSecret;
    public SmsCertificationController(){
        this.smsCertificationService = NurigoApp.INSTANCE.initialize(apiKey,apiSecret,"https://api.coolsms.co.kr");
    }
    @PostMapping("/sms-certification/sends")
    public ResponseEntity sendSms(@RequestBody UserDto.SmsCertificationRequest requestDto) {
        smsCertificationService.send(requestDto.getPhone());
        return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.SMS_CERT_MESSAGE_SUCCESS), HttpStatus.OK);
    }

    //인증번호 확인
    @PostMapping("/sms-certification/confirms")
    public ResponseEntity<Void> SmsVerification(@RequestBody UserDto.SmsCertificationRequest requestDto) {
        smsCertificationService.verifySms(requestDto);
        return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.SMS_CERT_SUCCESS),HttpStatus.OK);
    }




}
