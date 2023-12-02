package shop.mulmagi.app.web.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.domain.enums.NotificationType;
import shop.mulmagi.app.exception.CustomExceptions;
import shop.mulmagi.app.exception.ResponseMessage;
import shop.mulmagi.app.exception.StatusCode;
import shop.mulmagi.app.repository.UserRepository;
import shop.mulmagi.app.service.NotificationService;
import shop.mulmagi.app.web.controller.base.BaseController;
import shop.mulmagi.app.web.dto.NotificationResponseDto;
import shop.mulmagi.app.web.dto.base.DefaultRes;

import java.util.List;

@Api(tags = "알림 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NotificationController extends BaseController {
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @ApiOperation(value = "알림 내역 불러오기 API")
    @ApiResponse(code = 200, message = "알림 내역 불러오기 성공")
    @GetMapping("/notification/history")
    public ResponseEntity notificationHistory() {
        try {
            logger.info("Received request: method={}, path={}, description={}", "Get", "/api/notification/history", "알림 내역 불러오기 API");

            User user = userRepository.findByPhoneNumber("01043939869");

            notificationService.sendAndSaveNotification(user, NotificationType.OVERDUE, "연체입니다", "컨텐츠입니다");

            List<NotificationResponseDto.NotificationHistoryDto> res = notificationService.getNotificationHistory(user.getId());

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.NOTIFICATION_HISTORY_READ_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }
}
