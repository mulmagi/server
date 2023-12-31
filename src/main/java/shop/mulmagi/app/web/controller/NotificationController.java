package shop.mulmagi.app.web.controller;


import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.exception.CustomExceptions;
import shop.mulmagi.app.exception.ResponseMessage;
import shop.mulmagi.app.exception.StatusCode;
import shop.mulmagi.app.repository.UserRepository;
import shop.mulmagi.app.service.NotificationService;
import shop.mulmagi.app.service.UserService;
import shop.mulmagi.app.web.controller.base.BaseController;
import shop.mulmagi.app.web.dto.NotificationRequestDto;
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
    private final UserService userService;

    @ApiOperation(value = "전체 알림 내역 불러오기 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
    })
    @ApiResponse(code = 200, message = "전체 알림 내역 불러오기 성공")
    @GetMapping("/notification/history/all")
    public ResponseEntity notificationHistoryAll() {
        try {
            logger.info("Received request: method={}, path={}, description={}", "Get", "/api/notification/history/all", "전체 알림 내역 불러오기 API");

            User user = userService.getCurrentUser();

//            푸시알림 테스트 코드
//            notificationService.sendAndSaveNotification(user, NotificationType.OVERDUE, "연체입니다", "컨텐츠입니다");

            List<NotificationResponseDto.NotificationHistoryDto> res = notificationService.getNotificationHistoryAll(user.getId());

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.NOTIFICATION_HISTORY_ALL_READ_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "대여/반납/연체 알림 내역 불러오기 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
    })
    @ApiResponse(code = 200, message = "대여/반납/연체 알림 내역 불러오기 성공")
    @GetMapping("/notification/history/rental")
    public ResponseEntity notificationHistoryRental() {
        try {
            logger.info("Received request: method={}, path={}, description={}", "Get", "/api/notification/history/rental", "대여/반납/연체 알림 내역 불러오기 API");

            User user = userService.getCurrentUser();

            List<NotificationResponseDto.NotificationHistoryDto> res = notificationService.getNotificationHistoryRental(user);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.NOTIFICATION_HISTORY_RENTAL_READ_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "포인트 관련 알림 내역 불러오기 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
    })
    @ApiResponse(code = 200, message = "포인트 관련 알림 내역 불러오기 성공")
    @GetMapping("/notification/history/point")
    public ResponseEntity notificationHistoryPoint() {
        try {
            logger.info("Received request: method={}, path={}, description={}", "Get", "/api/notification/history/point", "포인트 관련 알림 내역 불러오기 API");

            User user = userService.getCurrentUser();

            List<NotificationResponseDto.NotificationHistoryDto> res = notificationService.getNotificationHistoryPoint(user);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.NOTIFICATION_HISTORY_POINT_READ_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "기타 알림 내역 불러오기 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
    })
    @ApiResponse(code = 200, message = "기타 알림 내역 불러오기 성공")
    @GetMapping("/notification/history/etc")
    public ResponseEntity notificationHistoryEtc() {
        try {
            logger.info("Received request: method={}, path={}, description={}", "Get", "/api/notification/history/etc", "기타 알림 내역 불러오기 API");

            User user = userService.getCurrentUser();

            List<NotificationResponseDto.NotificationHistoryDto> res = notificationService.getNotificationHistoryEtc(user);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.NOTIFICATION_HISTORY_ETC_READ_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "푸시 알림 허용하기 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
    })
    @ApiResponse(code = 200, message = "푸시 알림 허용하기 성공")
    @PostMapping("/notification/allow")
    public ResponseEntity notificationAllow(@RequestBody NotificationRequestDto.NotificationAllowRequestDto request) {
        try {
            logger.info("Received request: method={}, path={}, description={}", "Post", "/api/notification/allow", "푸시 알림 허용하기 API");

            User user = userService.getCurrentUser();
            String firebaseToken = request.getFirebaseToken();
            notificationService.saveFirebaseToken(user, firebaseToken);
            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.NOTIFICATION_ALLOW_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation(value = "푸시 알림 거부하기 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
    })
    @ApiResponse(code = 200, message = "푸시 알림 거부하기 성공")
    @PatchMapping("/notification/deny")
    public ResponseEntity notificationDeny() {
        try {
            logger.info("Received request: method={}, path={}, description={}", "Post", "/api/notification/deny", "푸시 알림 거부하기 API");

            User user = userService.getCurrentUser();
            notificationService.deleteFirebaseToken(user);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.NOTIFICATION_DENY_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }
}
