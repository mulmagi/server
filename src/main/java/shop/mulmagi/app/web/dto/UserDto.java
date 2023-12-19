package shop.mulmagi.app.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class UserDto {
    @Getter
    @Setter
    public static class NameRequest {
        private String name;

    }
    @Getter
    public static class SmsCertificationRequest {
        private String phone;
        private String certificationNumber;

        private SmsCertificationRequest() {
        }

        public static SmsCertificationRequestBuilder builder() {
            return new SmsCertificationRequestBuilder();
        }

        public static class SmsCertificationRequestBuilder {
            private String phone;
            private String certificationNumber;

            private SmsCertificationRequestBuilder() {
            }

            public SmsCertificationRequestBuilder phone(String phone) {
                this.phone = phone;
                return this;
            }

            public SmsCertificationRequestBuilder certificationNumber(String certificationNumber) {
                this.certificationNumber = certificationNumber;
                return this;
            }

            public SmsCertificationRequest build() {
                SmsCertificationRequest request = new SmsCertificationRequest();
                request.phone = this.phone;
                request.certificationNumber = this.certificationNumber;
                return request;
            }
        }
    }

    @Getter
    public static class WithdrawRequest {

        private String phoneNumber;

    }

    @Getter
    @Builder
    public static class RentalHistoryResponse{
        private String rentalUmbrellaStandName;
        private Long rentalUmbrellaStandId;
        private String returnUmbrellaStandName;
        private Long returnUmbrellaStandId;

        private Integer point;
        private Double experience;

        private LocalDateTime rentaldate;
        private LocalDateTime returndate;


    }

    @Getter
    @Builder
    public static class UserMenuResponse{
        private Integer level;
        private Double experience;
        private String userName;
        private String profileUrl;
        private String phoneNumber;
        private Integer point;
    }



}
