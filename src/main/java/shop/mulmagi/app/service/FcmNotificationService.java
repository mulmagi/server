package shop.mulmagi.app.service;

import shop.mulmagi.app.web.dto.NotificationRequestDto;

public interface FcmNotificationService {
    public void sendNotification(NotificationRequestDto.FcmRequestDto fcmRequestDto);
}
