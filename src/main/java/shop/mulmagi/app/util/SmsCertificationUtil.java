package shop.mulmagi.app.util;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import net.nurigo.sdk.message.model.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class SmsCertificationUtil {

        @Value("${spring.coolsms.senderNumber}")
        private String senderNumber;

        @Value("${spring.coolsms.apiKey}")
        private String apiKey;

        @Value("${spring.coolsms.apiSecret}")
        private String apiSecret;

        public DefaultMessageService messageService;

        public String certificationNumber;

    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

        public String makeTextContent(String certificationNumber){
            StringBuilder builder = new StringBuilder();
            builder.append("[물막이] 본인확인 인증번호는 ");
            builder.append(certificationNumber);
            builder.append("입니다. ");

            return builder.toString();
        }

        public Message makeMessage(String to) {
            int randomNumber = (int) (Math.random() * 9000) + 1000;
            certificationNumber = String.valueOf(randomNumber);
            Message message = new Message();
            message.setFrom(senderNumber);
            message.setTo(to);
            message.setText(makeTextContent(certificationNumber));
            return message;
        }


}
