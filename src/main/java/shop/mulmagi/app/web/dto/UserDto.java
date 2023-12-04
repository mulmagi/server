package shop.mulmagi.app.web.dto;

import lombok.Getter;
@Getter
public class UserDto {
    private String name;

    @Getter
    public static class SmsCertificationRequest {

        private String phone;
        private String certificationNumber;

    }

}
