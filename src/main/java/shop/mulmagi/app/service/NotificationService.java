package shop.mulmagi.app.service;

import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.domain.enums.NotificationType;
import shop.mulmagi.app.web.dto.NotificationResponseDto;

import java.util.List;

public interface NotificationService {
    public List<NotificationResponseDto.NotificationHistoryDto> getNotificationHistory(Long id);
    public void sendAndSaveNotification(User user, NotificationType type, String title, String body);
}
