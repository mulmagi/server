package shop.mulmagi.app.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shop.mulmagi.app.domain.Notification;
import shop.mulmagi.app.web.dto.NotificationResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NotificationConverter {
    public NotificationResponseDto.NotificationHistoryDto toNotificationHistoryDto(Notification notification) {
        return NotificationResponseDto.NotificationHistoryDto.builder()
                .type(notification.getType().getNotificationType())
                .mainContent(notification.getMainContent())
                .subContent(notification.getSubContent())
                .createdAt(notification.getCreatedAt())
                .build();
    }
    public List<NotificationResponseDto.NotificationHistoryDto> toNotificationHistoryDtoList(List<Notification> notificationList) {
        return notificationList.stream()
                .map(this::toNotificationHistoryDto)
                .collect(Collectors.toList());
    }
}