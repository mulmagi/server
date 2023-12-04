package shop.mulmagi.app.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.mulmagi.app.dao.CustomUserDetails;
import shop.mulmagi.app.domain.RefreshToken;
import shop.mulmagi.app.exception.CustomExceptions;
import shop.mulmagi.app.exception.ResponseMessage;
import shop.mulmagi.app.exception.StatusCode;
import shop.mulmagi.app.service.UserService;
import shop.mulmagi.app.util.JwtUtil;
import shop.mulmagi.app.web.controller.base.BaseController;
import shop.mulmagi.app.web.dto.UserDto;
import shop.mulmagi.app.web.dto.base.DefaultRes;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController extends BaseController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/name")
    public ResponseEntity<?> submitName(@RequestBody UserDto userDto) {
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
            log.info(ResponseMessage.SMS_CERT_SUCCESS);
            return login(requestDto);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto.SmsCertificationRequest requestDto) throws Exception {
        try {
            CustomUserDetails userDetails = userService.verifyAndRegisterUser(requestDto);
            String accessToken = jwtUtil.generateAccessToken(userDetails);
            String refreshTokenValue = jwtUtil.generateRefreshToken(userDetails);
            Date refreshExpTime = jwtUtil.calculateRefreshExpirationTime();

            RefreshToken refreshToken = RefreshToken.builder()
                    .user(userService.findByPhoneNumber(requestDto.getPhone()))
                    .token(refreshTokenValue)
                    .expirationTime(refreshExpTime)
                    .build();

            userService.saveRefreshToken(refreshToken);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", accessToken);
            log.info(ResponseMessage.ACCESS_TOKEN_ISSUE_SUCCESS + " : " + accessToken);

            tokens.put("refresh_token", refreshTokenValue);
            log.info(ResponseMessage.REFRESH_TOKEN_ISSUE_SUCCESS + " : " + refreshTokenValue);

            Map<String, Object> response = new HashMap<>();
            response.put("message", ResponseMessage.USER_LOGIN_SUCCESS);
            response.put("accessToken", accessToken);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{Id}/notifications")
    public ResponseEntity<?> updateNotificationSetting(@PathVariable Long Id,
                                                       @RequestParam boolean enableNotifications) throws Exception {
        try {
            userService.updateNotificationSettings(Id, enableNotifications);
            Map<String, Object> response = new HashMap<>();
            if (enableNotifications) {
                response.put("message", ResponseMessage.USER_AGREED_NOTIFICATION);
            } else {
                response.put("message", ResponseMessage.USER_DECLINE_NOTIFICATION);
            }
            response.put("id", String.valueOf(Id));

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody String refreshToken) {
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
    public ResponseEntity<String> logout(@RequestParam("accessToken") String accessToken, @RequestParam("refreshToken") String refreshToken) throws Exception {
        try {
            userService.logout(accessToken, refreshToken);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.USER_LOGOUT_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

}