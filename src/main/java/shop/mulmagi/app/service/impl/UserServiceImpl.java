package shop.mulmagi.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mulmagi.app.dao.CustomUserDetails;
import shop.mulmagi.app.dao.SmsCertificationDao;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.domain.enums.UserStatus;
import shop.mulmagi.app.exception.CustomExceptions;
import shop.mulmagi.app.repository.UserRepository;
import shop.mulmagi.app.service.UserService;
import shop.mulmagi.app.util.JwtUtil;
import shop.mulmagi.app.util.SmsCertificationUtil;
import shop.mulmagi.app.web.dto.UserDto;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SmsCertificationUtil smsUtil;
    private final SmsCertificationDao smsCertificationDao;

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;


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

    private void registerUser(UserDto.SmsCertificationRequest requestDto){
        if (isVerify(requestDto)) {
            User existingUser = userRepository.findByPhoneNumber(requestDto.getPhone());
            if (existingUser == null) {
                User user = User
                        .builder()
                        .name(requestDto.getPhone())
                        .phoneNumber(requestDto.getPhone())
                        .isAdmin(false)
                        .level(0)
                        .experience(0.0)
                        .point(0)
                        .profileUrl(" ")
                        .isRental(false)
                        .isComplaining(false)
                        .status(UserStatus.ACTIVE)
                        .build();
                userRepository.save(user);
            }
        }
    }
    public CustomUserDetails loadUserByPhoneNumber(String phoneNumber){
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new CustomExceptions.UserPhoneNumberNotFoundException("User not found with phone number: " + phoneNumber);
        }
        // User 객체를 UserDetails로 변환하여 반환
        return CustomUserDetails.builder()
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
                .authorities(getAuthorities(user.getIsAdmin()))
                .build();
    }
    private Collection<GrantedAuthority> getAuthorities(boolean isAdmin) {
        List<GrantedAuthority> authorityList = new ArrayList<>();

        if (isAdmin) {
            authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return authorityList;
    }

    public boolean verifyAndRegisterUser(UserDto.SmsCertificationRequest requestDto) {
        verifySms(requestDto);

        User user = findByPhoneNumber(requestDto.getPhone());
        boolean isNewUser = user == null;

        if (isNewUser) {
            registerUser(requestDto);
        }
        return isNewUser;
    }
    public void logout(String accessToken, String refreshToken) {
        jwtUtil.invalidateToken(accessToken);
        jwtUtil.invalidateToken(refreshToken);
    }


}

