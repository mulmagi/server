package shop.mulmagi.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mulmagi.app.dao.CustomUserDetails;
import shop.mulmagi.app.dao.SmsCertificationDao;
import shop.mulmagi.app.domain.RefreshToken;
import shop.mulmagi.app.domain.Rental;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.domain.enums.UserStatus;
import shop.mulmagi.app.exception.CustomExceptions;
import shop.mulmagi.app.repository.RefreshTokenRepository;
import shop.mulmagi.app.repository.RentalRepository;
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

    private final RentalRepository rentalRepository;


    private String storedName;

    public User findById(Long userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user;
        }
        return null;
    }


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
    private CustomUserDetails registerWithdrawUser(UserDto.SmsCertificationRequest requestDto){
        if (isVerify(requestDto)) {
            User existingUser = userRepository.findByPhoneNumber(requestDto.getPhone());
            if (existingUser != null && existingUser.getStatus() == INACTIVE) {
                existingUser.resetUser(storedName, requestDto.getPhone());
                userRepository.save(existingUser);
                return jwtUtil.buildCustomUserDetails(existingUser);
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
            throw new CustomExceptions.UserPhoneNumberNotFoundException("전화번호 " + phoneNumber + "를 사용하는 사용자가 없습니다.");
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
                .firebaseToken(user.getFirebaseToken())
                .agreeTerms(user.isAgreeTerms())
                .notificationEnabled(user.isNotificationEnabled())
                .build();
    }


    public CustomUserDetails verifyAndRegisterUser(UserDto.SmsCertificationRequest requestDto) {
        verifySms(requestDto);

        User user = findByPhoneNumber(requestDto.getPhone());
        boolean isNewUser = user == null;

        if (isNewUser) {
            return registerUser(requestDto);
        }
        else if (user.getStatus() == INACTIVE){
            return registerWithdrawUser(requestDto);
        }
        else {
            return loadUserByPhoneNumber(requestDto.getPhone());
        }
    }

    public void saveRefreshToken(String token) {
        Date tokenExpTime = jwtUtil.extractExpiration(token);
        String user_id = jwtUtil.extractId(token);
        Optional<User> optionalUser = userRepository.findById(Long.parseLong(user_id));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            RefreshToken refreshToken = RefreshToken
                    .builder()
                    .token(token)
                    .expirationTime(tokenExpTime)
                    .user(user)
                    .build();
            refreshTokenRepository.save(refreshToken);
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

    public void updateProfileImage(Long userId, String profileImageUrl) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.updateProfileURL(profileImageUrl);
            userRepository.save(user);
        }
    }
    public List<Rental>getUserRentals(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return rentalRepository.findByUserId(user.getId());
        }
        throw new CustomExceptions.NoRentalHistoryFoundException("no rental history");
    }
    public CustomUserDetails loadUserById(Long Id){
        Optional<User> optionalUser = userRepository.findById(Id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user == null) {
                throw new CustomExceptions.UserNotFoundException("사용자를 찾을 수 없습니다.");
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
                    .firebaseToken(user.getFirebaseToken())
                    .agreeTerms(user.isAgreeTerms())
                    .notificationEnabled(user.isNotificationEnabled())
                    .build();
        }
        return null;
    }


}

