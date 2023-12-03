package shop.mulmagi.app.service.impl;


import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.exception.CustomExceptions;
import shop.mulmagi.app.repository.UserRepository;
import shop.mulmagi.app.service.FcmNotificationService;
import shop.mulmagi.app.web.dto.NotificationRequestDto;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FcmNotificationServiceImpl implements FcmNotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;

    @Override
    public void sendNotification(NotificationRequestDto.FcmRequestDto fcmRequestDto) {

        Optional<User> user = userRepository.findById(fcmRequestDto.getTargetUserId());

        if (user.isPresent()) {
            if (user.get().getFirebaseToken() != null) {
                String firebaseToken = user.get().getFirebaseToken();

                Message message = Message.builder()
                        .setToken(firebaseToken)
                        .setNotification(Notification.builder()
                                .setTitle(fcmRequestDto.getTitle())
                                .setBody(fcmRequestDto.getBody())
                                .build())
                        .build();
                try {
                    firebaseMessaging.send(message);
                    System.out.println("알림을 성공적으로 전송했습니다. targetUserId=" + fcmRequestDto.getTargetUserId());
                } catch (FirebaseMessagingException e) {
                    e.printStackTrace();
                    throw new CustomExceptions.Exception("알림 보내기를 실패하였습니다. targetUserId=" + fcmRequestDto.getTargetUserId());
                }
            } else {
                throw new CustomExceptions.Exception("서버에 저장된 해당 유저의 FirebaseToken이 존재하지 않습니다. targetUserId=" + fcmRequestDto.getTargetUserId());
            }
        } else {
            throw new CustomExceptions.Exception("해당 유저가 존재하지 않습니다. targetUserId=" + fcmRequestDto.getTargetUserId());
        }
    }
}