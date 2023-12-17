package shop.mulmagi.app.service;

import shop.mulmagi.app.dao.CustomUserDetails;
import shop.mulmagi.app.domain.Rental;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.web.dto.UserDto;

import java.util.List;

public interface UserService {
    void sendSms(UserDto.SmsCertificationRequest requestDto);

    Long findIdByPhoneNumber(String phoneNumber);

    User findById(Long userId);
    Long loadUserByPhoneNumber(String phoneNumber);

    User findByPhoneNumber(String phoneNumber);

    Long verifyAndRegisterUser(UserDto.SmsCertificationRequest requestDto);
    void logout(String accessToken, String refreshToken);
    void updateNotificationSettings(Long userId, boolean enableNotifications);

    void submitName(String name);

    void withdrawUserByPhoneNumber(String phoneNumber);
    void updateProfileImage(Long userId, String profileImageUrl);
    List<Rental> getUserRentals(Long userId);
    User getCurrentUser();

    void saveRefreshToken(String token);

    CustomUserDetails loadUserById(Long id);
}
