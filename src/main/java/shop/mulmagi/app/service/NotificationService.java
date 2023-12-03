package shop.mulmagi.app.service;

import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.domain.enums.NotificationType;
import shop.mulmagi.app.web.dto.NotificationResponseDto;

import java.util.List;

public interface NotificationService {
    public List<NotificationResponseDto.NotificationHistoryDto> getNotificationHistoryAll(Long id);
    public List<NotificationResponseDto.NotificationHistoryDto> getNotificationHistoryRental(User user);
    public List<NotificationResponseDto.NotificationHistoryDto> getNotificationHistoryPoint(User user);
    public List<NotificationResponseDto.NotificationHistoryDto> getNotificationHistoryEtc(User user);

    public void sendAndSaveNotification(User user, NotificationType type, String title, String body);

    public void saveFirebaseToken(User user, String firebaseToken);
    public void deleteFirebaseToken(User user);
}
