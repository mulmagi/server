package shop.mulmagi.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mulmagi.app.converter.NotificationConverter;
import shop.mulmagi.app.domain.Notification;
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
    public List<NotificationResponseDto.NotificationHistoryDto> getNotificationHistory(Long id){
        List<Notification> notificationList = notificationRepository.findByUserId(id);

        return notificationConverter.toNotificationHistoryDtoList(notificationList);
    }
}
