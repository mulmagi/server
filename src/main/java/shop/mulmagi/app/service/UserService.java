package shop.mulmagi.app.service;

import org.mapstruct.control.MappingControl;
import shop.mulmagi.app.dao.CustomUserDetails;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.web.dto.UserDto;

public interface UserService {
    void sendSms(UserDto.SmsCertificationRequest requestDto);
    CustomUserDetails loadUserByPhoneNumber(String phoneNumber);

    User findByPhoneNumber(String phoneNumber);

    boolean verifyAndRegisterUser(UserDto.SmsCertificationRequest requestDto);
    void logout(String accessToken);
    void deleteUser(String accessToken);

}
