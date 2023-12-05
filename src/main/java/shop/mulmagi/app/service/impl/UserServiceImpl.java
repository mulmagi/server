package shop.mulmagi.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import shop.mulmagi.app.dao.CustomUserDetails;
import shop.mulmagi.app.dao.SmsCertificationDao;
import shop.mulmagi.app.domain.RefreshToken;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.domain.enums.UserStatus;
import shop.mulmagi.app.exception.CustomExceptions;
import shop.mulmagi.app.repository.RefreshTokenRepository;
import shop.mulmagi.app.repository.UserRepository;
import shop.mulmagi.app.service.UserService;
import shop.mulmagi.app.util.JwtUtil;
import shop.mulmagi.app.util.SmsCertificationUtil;
import shop.mulmagi.app.web.dto.UserDto;

import java.util.*;

import static shop.mulmagi.app.domain.enums.UserStatus.INACTIVE;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SmsCertificationUtil smsUtil;
    private final SmsCertificationDao smsCertificationDao;

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private String storedName;


    public void sendSms(UserDto.SmsCertificationRequest requestDto){
        String to = requestDto.getPhone();
        int randomNumber = (int) (Math.random() * 9000) + 1000;
        String certificationNumber = String.valueOf(randomNumber);
        smsUtil.sendSms(to, certificationNumber);
        smsCertificationDao.createSmsCertification(to,certificationNumber);
    }

    private void verifySms(UserDto.SmsCertificationRequest requestDto) {
        if (isVerify(requestDto)) {
            throw new CustomExceptions.SmsCertificationNumberMismatchException("인증번호가 일치하지 않습니다.");
        }
        smsCertificationDao.removeSmsCertification(requestDto.getPhone());
    }

    private boolean isVerify(UserDto.SmsCertificationRequest requestDto) {
        return !(smsCertificationDao.hasKey(requestDto.getPhone()) &&
                smsCertificationDao.getSmsCertification(requestDto.getPhone())
                        .equals(requestDto.getCertificationNumber()));
    }

    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    private CustomUserDetails registerUser(UserDto.SmsCertificationRequest requestDto){
        if (isVerify(requestDto)) {
            User existingUser = userRepository.findByPhoneNumber(requestDto.getPhone());
            if (existingUser == null) {
                User user = User
                        .builder()
                        .name(storedName)
                        .phoneNumber(requestDto.getPhone())
                        .isAdmin(false)
                        .level(0)
                        .experience(0.0)
                        .point(0)
                        .profileUrl(" ")
                        .isRental(false)
                        .isComplaining(false)
                        .status(UserStatus.ACTIVE)
                        .notificationEnabled(false)
                        .agreeTerms(false)
                        .build();
                userRepository.save(user);
                return jwtUtil.buildCustomUserDetails(user);
            }
        }
        return null;
    }


    public void submitName(String name){
        this.storedName = name;
    }

    public void updateNotificationSettings(Long userId, boolean enableNotifications) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.updateNotificationEnabled(enableNotifications);
                userRepository.save(user);
        }
    }
    public CustomUserDetails loadUserByPhoneNumber(String phoneNumber){
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new CustomExceptions.UserPhoneNumberNotFoundException("User not found with phone number: " + phoneNumber);
        }
        // User 객체를 UserDetails로 변환하여 반환
        return CustomUserDetails.builder()
                .name(storedName)
                .id(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .isAdmin(user.getIsAdmin())
                .level(user.getLevel())
                .experience(user.getExperience())
                .point(user.getPoint())
                .profileUrl(user.getProfileUrl())
                .isRental(user.getIsRental())
                .status(user.getStatus())
                .isComplaining(user.getIsComplaining())
                .authorities(jwtUtil.getAuthorities(user.getIsAdmin()))
                .build();
    }


    public CustomUserDetails verifyAndRegisterUser(UserDto.SmsCertificationRequest requestDto) {
        verifySms(requestDto);

        User user = findByPhoneNumber(requestDto.getPhone());
        boolean isNewUser = user == null;

        if (isNewUser) {
            return registerUser(requestDto);
        }else{
            return loadUserByPhoneNumber(requestDto.getPhone());
        }
    }

    public void saveRefreshToken(RefreshToken refreshToken) {
        if (refreshToken != null) {
            RefreshToken existingToken = refreshTokenRepository.findByUser(refreshToken.getUser());
            if (existingToken != null) {
                existingToken.updateToken(refreshToken.getToken());
                existingToken.updateExpirationTime(refreshToken.getExpirationTime());
            } else {
                refreshTokenRepository.save(refreshToken);
            }
        }
    }




    public void logout(String accessToken, String refreshToken) {
        jwtUtil.invalidateToken(accessToken);
        jwtUtil.invalidateToken(refreshToken);
    }


    public void withdrawUserByPhoneNumber(String phoneNumber){
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user != null) {
            user.updateStatus(INACTIVE);
            userRepository.save(user);
        }
    }


}

