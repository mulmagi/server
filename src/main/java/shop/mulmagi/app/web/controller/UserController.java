package shop.mulmagi.app.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.mulmagi.app.domain.Rental;
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

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController extends BaseController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/name")
    public ResponseEntity<?> submitName(@RequestBody UserDto.NameRequest userDto) {
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

    @PostMapping("/sms-certification/send")
    public ResponseEntity<?> sendSms(@RequestBody UserDto.SmsCertificationRequest requestDto) throws Exception {
        try {
            userService.sendSms(requestDto);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.SMS_CERT_MESSAGE_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    //인증번호 확인
    @PostMapping("/sms-certification/confirm")
    public ResponseEntity<?> smsConfirm(@RequestBody UserDto.SmsCertificationRequest requestDto) throws Exception {
        try {
            Long userId = userService.verifyAndRegisterUser(requestDto);
            String accessToken = jwtUtil.generateAccessToken(userId);
            String refreshToken = jwtUtil.generateRefreshToken(userId);

            userService.saveRefreshToken(refreshToken);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            log.info(ResponseMessage.REFRESH_TOKEN_ISSUE_SUCCESS + refreshToken);
            log.info(ResponseMessage.ACCESS_TOKEN_ISSUE_SUCCESS + accessToken);
            log.info(ResponseMessage.ACCESS_TOKEN_SEND_SUCCESS);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.USER_LOGIN_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/notifications")
    public ResponseEntity<?> updateNotificationSetting(@RequestParam boolean enableNotifications) throws Exception {
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

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(String refreshToken) {
        User user = userService.getCurrentUser();

        String newAccessToken = jwtUtil.generateAccessTokenFromRefreshToken(refreshToken);

        if (newAccessToken != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", ResponseMessage.ACCESS_TOKEN_REISSUE_SUCCESS);
            response.put("accessToken", newAccessToken);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body("Failed to reissue access token");
        }
    }


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
    @PutMapping("/profile-image")
    public ResponseEntity<?> setUserProfileImage(@RequestParam String profileImageUrl) {
        try {
            User user = userService.getCurrentUser();
            userService.updateProfileImage(user.getId(),profileImageUrl);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.PROFILE_IMAGE_UPLOAD_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/rental-history")
    public ResponseEntity<?> getUserRentals(@RequestParam(value = "cursor", required = false) String cursor) {
        try {
            User user = userService.getCurrentUser();
            log.info("currentUser id = "+ user.getId());
            List<UserDto.RentalHistoryResponse> rentals = userService.getRentalHistory(user);

            // 가져온 다음 페이지의 항목과 다음 커서를 클라이언트에 전달

            Map<String, Object> response = new HashMap<>();
            response.put("message : ", ResponseMessage.PRINT_RENTAL_HISTORY_SUCCESS);
            response.put("rentalHistory : ", rentals);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }


}
