package shop.mulmagi.app.service.impl;

import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import net.nurigo.sdk.message.service.MessageHttpService;
import org.springframework.beans.factory.annotation.Value;
import net.nurigo.sdk.message.model.Message;
import shop.mulmagi.app.service.SmsCertifiactionService;
import shop.mulmagi.app.web.dto.UserDto;
import shop.mulmagi.app.dao.smsCertificationDao;

@RequiredArgsConstructor
abstract class SmsCertifiactionServiceImpl implements SmsCertifiactionService {
    private final DefaultMessageService defaultMessageService;
    @Value("coolsms.senderNumber")
    private String senderNumber;

    public String makeTextContent(String certificationNumber){
        StringBuilder builder = new StringBuilder();
        builder.append("[물막이] 본인확인 인증번호는 ");
        builder.append(certificationNumber);
        builder.append("입니다. ");

        return builder.toString();
    }
    public String generateRandomNumber(){
        int randomNumber = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(randomNumber);
    }


    public Message makeMessage(String to) {
        String certificationNumber = generateRandomNumber();
        Message message = new Message();
        message.setFrom(senderNumber);
        message.setTo(to);
        message.setText(makeTextContent(certificationNumber));
        return message;
    }

    public void sendSms(String phone) {
        Message message = makeMessage(phone);
        defaultMessageService.sendOne(new SingleMessageSendingRequest(message));
    }

    public boolean verifySms(UserDto.SmsCertificationRequest requestDto) {

        String storedCode = smsCertificationDao.getSmsCertification(requestDto.getPhone());

        // Step 2: Compare the stored code with the input code
        return requestDto.getCertificationNumber().equals(storedCode);
    }



}
