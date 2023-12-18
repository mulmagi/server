package shop.mulmagi.app.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import shop.mulmagi.app.domain.UmbrellaStand;

import java.time.LocalDateTime;

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

    @Getter
    @Builder
    public static class RentalHistoryResponse{
        private String rentalUmbrellaStandName;
        private String returnUmbrellaStandName;

        private Integer point;
        private Double experience;

        private LocalDateTime rentaldate;
        private LocalDateTime returndate;


    }



}
