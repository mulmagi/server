package shop.mulmagi.app.service;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import shop.mulmagi.app.web.dto.UserDto;

import java.util.HashMap;

public interface SmsCertifiactionService {
    public String makeTextContent(String certificationNumber);
    public String generateRandomNumber();
    public Message makeMessage(String to);

    public void sendSms(String phone);
    public boolean verifySms(UserDto.SmsCertificationRequest requestDto);



}
