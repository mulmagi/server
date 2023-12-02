package shop.mulmagi.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mulmagi.app.converter.NotificationConverter;
import shop.mulmagi.app.domain.Notification;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.domain.enums.NotificationType;
import shop.mulmagi.app.repository.NotificationRepository;
import shop.mulmagi.app.service.NotificationService;
import shop.mulmagi.app.web.dto.NotificationResponseDto;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationConverter notificationConverter;
    @Override
    public List<NotificationResponseDto.NotificationHistoryDto> getNotificationHistory(Long userId){
        List<Notification> notificationList = notificationRepository.findByUserId(userId);

        return notificationConverter.toNotificationHistoryDtoList(notificationList);
    }

    @Override
    public void sendAndSaveNotification(User user, NotificationType type, String mainContent, String subContent){
        // 푸시 알림 보내기

        // 알림 내역 저장
        Notification notification =
                Notification.builder()
                .user(user)
                .type(type)
                .mainContent(mainContent)
                .subContent(subContent)
                .build();

        notificationRepository.save(notification);
    }
}
