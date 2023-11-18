package shop.mulmagi.app.service;

import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import shop.mulmagi.app.web.dto.UserDto;

public interface UserService {
    SingleMessageSentResponse sendSms(String phone);
    void verifySms(UserDto.SmsCertificationRequest requestDto);
    boolean isVerify(UserDto.SmsCertificationRequest requestDto);

}
