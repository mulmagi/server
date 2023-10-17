package shop.mulmagi.app.web.dto;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class UmbrellaResponseDto {

    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LocationDto{
        private Long locationId;
        private String name;
        private Integer umbrellaCount;
        private List<Integer> umbrellaNumber;
    }

    @Builder @Data
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RentalPageDto{
        private Boolean isUmbrella;
        private Boolean isWrong;
        private String rentalUmbrellaStand;
        private Integer userPoint;
    }

    @Builder @Data
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReturnPageDto{
        private Long rentalId;
        private LocalDateTime rentalTime;
        private LocalDateTime returnTime;
        private String rentalUmbrellaStand;
        private String returnUmbrellaStand;
        private Integer overDueAmount;
    }
}
