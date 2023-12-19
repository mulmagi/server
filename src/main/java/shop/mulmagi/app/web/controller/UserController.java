package shop.mulmagi.app.web.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.exception.CustomExceptions;
import shop.mulmagi.app.exception.ResponseMessage;
import shop.mulmagi.app.exception.StatusCode;
import shop.mulmagi.app.service.UserService;
import shop.mulmagi.app.util.JwtUtil;
import shop.mulmagi.app.web.controller.base.BaseController;
import shop.mulmagi.app.web.dto.UserDto;
import shop.mulmagi.app.web.dto.base.DefaultRes;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController extends BaseController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    @ApiOperation(value = "이름 입력하는 API")
    @PostMapping("/name")
    public ResponseEntity<?> submitName(@ApiParam(value = "이름", example = "{\"name\": \"최유정\"}") @RequestBody UserDto.NameRequest userDto) {
        try {
            userService.submitName(userDto.getName());
            Map<String, Object> response = new HashMap<>();
            response.put("message", ResponseMessage.NAME_SUBMIT_SUCCESS);
            response.put("name", userDto.getName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @ApiOperation(value = "SMS 인증 문자 보내는 API")
    @PostMapping("/sms-certification/send")
    public ResponseEntity<?> sendSms(@ApiParam(value = "문자 전송", example = "{\"number\": \"01012345678\",\"certificationNumber\": \"\"}") @RequestBody UserDto.SmsCertificationRequest requestDto) throws Exception {
        try {
            userService.sendSms(requestDto);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.SMS_CERT_MESSAGE_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    //인증번호 확인
    @ApiOperation(value = "인증번호 확인하고 로그인하는 API")
    @PostMapping("/sms-certification/confirm")
    public ResponseEntity<?> smsConfirm(@ApiParam(value = "인증 번호 확인", example = "{\"number\": \"01012345678\",\"certificationNumber\": \"5678\"}")@RequestBody UserDto.SmsCertificationRequest requestDto) throws Exception {
        try {
            Long userId = userService.verifyAndRegisterUser(requestDto);
            String accessToken = jwtUtil.generateAccessToken(userId);
            String refreshToken = jwtUtil.generateRefreshToken(userId);

            userService.saveRefreshToken(refreshToken);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            log.info(ResponseMessage.REFRESH_TOKEN_ISSUE_SUCCESS + refreshToken);
            Map<String, Object> response = new HashMap<>();
            response.put("message", ResponseMessage.ACCESS_TOKEN_REISSUE_SUCCESS);
            response.put("accessToken", accessToken);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }
    @ApiOperation(value = "알림 설정 허용하는 API")
    @PutMapping("/notifications")
    public ResponseEntity<?> updateNotificationSetting(@RequestParam @ApiParam(value="enableNotifications", example = "true")boolean enableNotifications) throws Exception {
        try {
            User user = userService.getCurrentUser();
            userService.updateNotificationSettings(user.getId(), enableNotifications);
            Map<String, Object> response = new HashMap<>();
            if (enableNotifications) {
                response.put("message", ResponseMessage.USER_AGREED_NOTIFICATION);
            } else {
                response.put("message", ResponseMessage.USER_DECLINE_NOTIFICATION);
            }
            response.put("id", String.valueOf(user.getId()));

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }
    @ApiOperation(value = "AccessToken 재발급하는 API")
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue() {
        User user = userService.getCurrentUser();

        String newAccessToken = userService.reissueAccessToken(user);

        if (newAccessToken != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", ResponseMessage.ACCESS_TOKEN_REISSUE_SUCCESS);
            response.put("accessToken", newAccessToken);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body("Failed to reissue access token");
        }
    }

    @ApiOperation(value = "회원 로그아웃하는 API")
    @PostMapping("/logout")
    public ResponseEntity<String> logout()throws Exception {
        try {
            User user = userService.getCurrentUser();
            userService.logout(user);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.USER_LOGOUT_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }
    @ApiOperation(value = "회원 탈퇴하는 API")
    @PutMapping("/withdraw")
    public ResponseEntity<?> withdrawUserByPhoneNumber(){
        try {
            User user = userService.getCurrentUser();
            userService.withdrawUser(user);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.USER_DELETION_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }
    @ApiOperation(value = "회원 프로필 이미지 변경하는 API")
    @PutMapping("/profile-image")
    public ResponseEntity<?> setUserProfileImage(@RequestParam @ApiParam(value="profileImageUrl", example = "https://url.kr/liwqn2")String profileImageUrl) {
        try {
            User user = userService.getCurrentUser();
            userService.updateProfileImage(user.getId(),profileImageUrl);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.PROFILE_IMAGE_UPLOAD_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }
    @ApiOperation(value = "회원 대여 기록 가져오는 API")
    @GetMapping("/user/rental-history")
    public ResponseEntity<?> getUserRentals(@RequestParam(value = "cursor", required = false)
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @ApiParam(value="cursor",example = "2023-12-18T06:23:27")LocalDateTime cursor) {
        int pageSize = 6;
        try {
            User user = userService.getCurrentUser();
            List<UserDto.RentalHistoryResponse> rentals = userService.getRentalHistory(user, pageSize, cursor);

            LocalDateTime nextCursor = userService.getNextCursor(user,cursor);

            Map<String, Object> response = new HashMap<>();
            response.put("message : ", ResponseMessage.PRINT_RENTAL_HISTORY_SUCCESS);
            response.put("rentalHistory : ", rentals);
            response.put("nextCursor", nextCursor);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "회원 정보를 메뉴화면에 보여주는 API")
    @GetMapping("/user/menu")
    public ResponseEntity<?> loadUserInfo(){
        User user = userService.getCurrentUser();
        try{
            UserDto.UserMenuResponse res = userService.loadUserInfo(user);
            Map<String, Object> response = new HashMap<>();
            response.put("message : ", ResponseMessage.USER_INFO_LOAD_SUCCESS);
            response.put("UserInfo : ", res);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }


}
