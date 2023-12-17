package shop.mulmagi.app.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UserDto {
    @Getter
    @Setter
    public static class NameRequest {
        private String name;

    }
    @Getter
    @Builder
    public static class SmsCertificationRequest {

        private String phone;
        private String certificationNumber;

    }

    @Getter
    public static class WithdrawRequest {

        private String phoneNumber;

    }



}
