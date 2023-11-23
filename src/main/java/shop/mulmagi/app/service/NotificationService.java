package shop.mulmagi.app.service;

import shop.mulmagi.app.web.dto.NotificationResponseDto;

import java.util.List;

public interface NotificationService {
    public List<NotificationResponseDto.NotificationHistoryDto> getNotificationHistory(Long id);
}
