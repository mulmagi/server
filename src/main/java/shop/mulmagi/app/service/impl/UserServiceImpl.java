package shop.mulmagi.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import shop.mulmagi.app.dao.CustomUserDetails;
import shop.mulmagi.app.dao.SmsCertificationDao;
import shop.mulmagi.app.domain.*;
import shop.mulmagi.app.domain.enums.UserStatus;
import shop.mulmagi.app.exception.CustomExceptions;
import shop.mulmagi.app.exception.ResponseMessage;
import shop.mulmagi.app.repository.RefreshTokenRepository;
import shop.mulmagi.app.repository.RentalRepository;
import shop.mulmagi.app.repository.UserRepository;
import shop.mulmagi.app.service.UserService;
import shop.mulmagi.app.util.JwtUtil;
import shop.mulmagi.app.util.SmsCertificationUtil;
import shop.mulmagi.app.web.dto.UserDto;

import java.util.*;

import static shop.mulmagi.app.domain.enums.UserStatus.INACTIVE;
@Slf4j
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

    public void submitName(String name){
        this.storedName = name;
    }


    public void sendSms(UserDto.SmsCertificationRequest requestDto){
        String to = requestDto.getPhone();
        int randomNumber = (int) (Math.random() * 9000) + 1000;
        String certificationNumber = String.valueOf(randomNumber);
        smsUtil.sendSms(to, certificationNumber);
        smsCertificationDao.createSmsCertification(to,certificationNumber);
    }


    public User findById(Long userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user;
        }
        return null;
    }



    private void verifySms(UserDto.SmsCertificationRequest requestDto) {
        if (isVerify(requestDto)) {
            throw new CustomExceptions.SmsCertificationNumberMismatchException("인증번호가 일치하지 않습니다.");
        }
        log.info(ResponseMessage.SMS_CERT_SUCCESS);
        smsCertificationDao.removeSmsCertification(requestDto.getPhone());
    }

    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public Long findIdByPhoneNumber(String phoneNumber){return findByPhoneNumber(phoneNumber).getId();}




    private boolean isVerify(UserDto.SmsCertificationRequest requestDto) {
        return !(smsCertificationDao.hasKey(requestDto.getPhone()) &&
                smsCertificationDao.getSmsCertification(requestDto.getPhone())
                        .equals(requestDto.getCertificationNumber()));
    }


    public void updateNotificationSettings(Long userId, boolean enableNotifications) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.updateNotificationEnabled(enableNotifications);
                userRepository.save(user);
        }
    }
    private Long registerUser(UserDto.SmsCertificationRequest requestDto){
        if (isVerify(requestDto)) {
            User existingUser = findByPhoneNumber(requestDto.getPhone());
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
                log.info("회원가입 성공");
                return user.getId();
            }
        }
        throw new CustomExceptions.Exception("회원가입을 할 수 없습니다.");
    }
    private Long registerWithdrawUser(UserDto.SmsCertificationRequest requestDto){
        if (isVerify(requestDto)) {
            User existingUser = userRepository.findByPhoneNumber(requestDto.getPhone());
            if (existingUser != null && existingUser.getStatus() == INACTIVE) {
                existingUser.resetUser(storedName, requestDto.getPhone());
                userRepository.save(existingUser);
                return existingUser.getId();
            }
        }
        throw new CustomExceptions.Exception("탈퇴한 유저를 되돌릴 수 없습니다.");
    }
    public Long loadUserByPhoneNumber(String phoneNumber){
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new CustomExceptions.UserPhoneNumberNotFoundException("전화번호 " + phoneNumber + "를 사용하는 사용자가 없습니다.");
        }
        return user.getId();
    }


    public Long verifyAndRegisterUser(UserDto.SmsCertificationRequest requestDto) {
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
        Long user_id = jwtUtil.extractId(token);
        Optional<User> optionalUser = userRepository.findById(user_id);
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

    public void logout(User user) {
        RefreshToken token = refreshTokenRepository.findByUser(user);
        if (token != null) {
            String refreshToken = token.getToken();
            jwtUtil.invalidateRefreshToken(refreshToken);
            refreshTokenRepository.delete(token);
        }

    }


    public void withdrawUser(User user){
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
    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "로그인 되지 않았습니다."
            );
        }
        Long userId = (Long)authentication.getPrincipal();
        User user = findById(userId);

        if(user.getStatus().equals(UserStatus.INACTIVE)) {
            throw new IllegalArgumentException("해당 사용자는 탈퇴한 사용자입니다.");
        }
        return user;
    }
    public List<UserDto.RentalHistoryResponse> getRentalHistory(User user){
        List<UserDto.RentalHistoryResponse> rentalHistoryResponses = new ArrayList<>();
        List<Rental> userRentals = rentalRepository.findByUserId(user.getId());

        for (Rental rental : userRentals) {
            log.info(rental.getUser().getId().toString());
            UmbrellaStand returnStand = rental.getReturnUmbrellaStand();
            if (returnStand != null) {
                Location returnLocation = returnStand.getLocation();
                Long returnID = returnStand.getId();
                if (returnLocation != null) {
                    String returnUmbrellaStandLocationName = returnLocation.getName();
                    log.info(returnUmbrellaStandLocationName);
                    UmbrellaStand rentalStand = rental.getRentalUmbrellaStand();
                    Location rentalLocation = rentalStand.getLocation();
                    Long rentalId = rentalStand.getId();
                    String rentalUmbrellaStandLocationName = rentalLocation.getName();
                    UserDto.RentalHistoryResponse response =
                            UserDto.RentalHistoryResponse
                                    .builder()
                                    .rentalUmbrellaStandName(rentalUmbrellaStandLocationName)
                                    .rentalUmbrellaStandId(rentalId)
                                    .returnUmbrellaStandName(returnUmbrellaStandLocationName)
                                    .returnUmbrellaStandId(returnID)
                                    .rentaldate(rental.getCreatedAt())
                                    .returndate(rental.getUpdatedAt())
                                    .point(rental.getOverdueAmount() + 1000)
                                    .experience(rental.getUser().getExperience())
                                    .build();
                    // Add other fields from Rental to RentalHistoryResponse
                    rentalHistoryResponses.add(response);
                }
            }

        }
        return rentalHistoryResponses;
    }
}

