package shop.mulmagi.app.service.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
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

import java.util.ArrayList;
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
        List<Notification> notificationList = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return notificationConverter.toNotificationHistoryDtoList(notificationList);
    }

    @Override
    public List<NotificationResponseDto.NotificationHistoryDto> getNotificationHistoryRental(User user){
        List<NotificationType> rentalList = new ArrayList<>();

        rentalList.add(NotificationType.RENTAL);
        rentalList.add(NotificationType.RETURN);
        rentalList.add(NotificationType.OVERDUE);

        List<Notification> notificationList = notificationRepository.findRentalByUser(user, rentalList);

        return notificationConverter.toNotificationHistoryDtoList(notificationList);
    }

    @Override
    public List<NotificationResponseDto.NotificationHistoryDto> getNotificationHistoryPoint(User user){
        List<NotificationType> pointList = new ArrayList<>();

        pointList.add(NotificationType.REFUND_POINT);
        pointList.add(NotificationType.USE_POINT);
        pointList.add(NotificationType.PAYMENT);

        List<Notification> notificationList = notificationRepository.findPointByUser(user, pointList);

        return notificationConverter.toNotificationHistoryDtoList(notificationList);
    }

    @Override
    public List<NotificationResponseDto.NotificationHistoryDto> getNotificationHistoryEtc(User user){
        List<NotificationType> etcList = new ArrayList<>();

        etcList.add(NotificationType.ANNOUNCE);
        etcList.add(NotificationType.REPORT);
        etcList.add(NotificationType.ETC);

        List<Notification> notificationList = notificationRepository.findEtcByUser(user, etcList);

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
    public void saveFirebaseToken(User user, String firebaseToken) throws FirebaseAuthException {
        FirebaseAuth.getInstance().verifyIdToken(firebaseToken);
        userRepository.updateFirebaseToken(user.getId(), firebaseToken);
    }

    @Override
    public void deleteFirebaseToken(User user){
        userRepository.deleteFirebaseToken(user.getId());
    }

}
