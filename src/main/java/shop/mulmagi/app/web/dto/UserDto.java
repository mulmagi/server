package shop.mulmagi.app.web.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

public class UserDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SmsCertificationRequest {

        private String phone;
        private String certificationNumber;

        @Builder
        public SmsCertificationRequest(String phone, String certificationNumber) {
            this.phone = phone;
            this.certificationNumber = certificationNumber;
        }

    }
}
