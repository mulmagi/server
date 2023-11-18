package shop.mulmagi.app.service.impl;

import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mulmagi.app.dao.SmsCertificationDao;
import shop.mulmagi.app.exception.CustomExceptions;
import shop.mulmagi.app.service.UserService;
import shop.mulmagi.app.util.SmsCertificationUtil;
import shop.mulmagi.app.web.dto.UserDto;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SmsCertificationUtil smsUtil;
    private final SmsCertificationDao smsCertificationDao;

    public SingleMessageSentResponse sendSms(String phone) {
        Message message = smsUtil.makeMessage(phone);
        SingleMessageSentResponse response = smsUtil.messageService.sendOne(new SingleMessageSendingRequest(message));
        smsCertificationDao.createSmsCertification(phone, smsUtil.certificationNumber);
        return response;

    }

    public void verifySms(UserDto.SmsCertificationRequest requestDto) {
        if (isVerify(requestDto)) {
            throw new CustomExceptions.SmsCertificationNumberMismatchException("인증번호가 일치하지 않습니다.");
        }
        smsCertificationDao.removeSmsCertification(requestDto.getPhone());
    }

    public boolean isVerify(UserDto.SmsCertificationRequest requestDto) {
        return !(smsCertificationDao.hasKey(requestDto.getPhone()) &&
                smsCertificationDao.getSmsCertification(requestDto.getPhone())
                        .equals(requestDto.getCertificationNumber()));
    }

}
