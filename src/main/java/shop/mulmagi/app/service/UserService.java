package shop.mulmagi.app.service;

import shop.mulmagi.app.web.dto.UserDto;

public interface UserService {
    void sendSms(UserDto.SmsCertificationRequest requestDto);
    void verifySms(UserDto.SmsCertificationRequest requestDto);
    boolean isVerify(UserDto.SmsCertificationRequest requestDto);
    void registerMember(UserDto.SmsCertificationRequest requestDto);

}
