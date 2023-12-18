package shop.mulmagi.app.service;

import shop.mulmagi.app.dao.CustomUserDetails;
import shop.mulmagi.app.domain.Rental;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.web.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {
    void sendSms(UserDto.SmsCertificationRequest requestDto);

    Long findIdByPhoneNumber(String phoneNumber);

    User findById(Long userId);
    Long loadUserByPhoneNumber(String phoneNumber);

    User findByPhoneNumber(String phoneNumber);

    Long verifyAndRegisterUser(UserDto.SmsCertificationRequest requestDto);
    void logout(User user);
    void updateNotificationSettings(Long userId, boolean enableNotifications);

    void submitName(String name);

    void withdrawUser(User user);
    void updateProfileImage(Long userId, String profileImageUrl);
    List<Rental> getUserRentals(Long userId);
    User getCurrentUser();

    void saveRefreshToken(String token);

    CustomUserDetails loadUserById(Long id);

    String reissueAccessToken(User user);

    List<UserDto.RentalHistoryResponse> getRentalHistory(User user, int pageSize, LocalDateTime cursor);
    LocalDateTime getNextCursor(User user, LocalDateTime cursor);

}
