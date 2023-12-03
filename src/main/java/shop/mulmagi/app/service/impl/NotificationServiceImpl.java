package shop.mulmagi.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mulmagi.app.converter.NotificationConverter;
import shop.mulmagi.app.domain.Notification;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.domain.enums.NotificationType;
import shop.mulmagi.app.repository.NotificationRepository;
import shop.mulmagi.app.repository.UserRepository;
import shop.mulmagi.app.service.FcmNotificationService;
import shop.mulmagi.app.service.NotificationService;
import shop.mulmagi.app.web.dto.NotificationRequestDto;
import shop.mulmagi.app.web.dto.NotificationResponseDto;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final FcmNotificationService fcmNotificationService;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationConverter notificationConverter;
    @Override
    public List<NotificationResponseDto.NotificationHistoryDto> getNotificationHistoryAll(Long userId){
        List<Notification> notificationList = notificationRepository.findByUserId(userId);

        return notificationConverter.toNotificationHistoryDtoList(notificationList);
    }

    @Override
    public void sendAndSaveNotification(User user, NotificationType type, String title, String body){

        // FcmRequestDto 생성
        NotificationRequestDto.FcmRequestDto fcmRequestDto
                = NotificationRequestDto.FcmRequestDto.builder()
                .targetUserId(user.getId())
                .title(title)
                .body(body)
                .build();

        // 푸시 알림 보내기
        fcmNotificationService.sendNotification(fcmRequestDto);

        // 알림 내역 저장
        Notification notification =
                Notification.builder()
                        .user(user)
                        .type(type)
                        .title(title)
                        .body(body)
                        .build();

        notificationRepository.save(notification);
    }

    @Override
    public void saveFirebaseToken(User user, String firebaseToken){
        userRepository.updateFirebaseToken(user.getId(), firebaseToken);
    }

    @Override
    public void deleteFirebaseToken(User user){
        userRepository.deleteFirebaseToken(user.getId());
    }

}
