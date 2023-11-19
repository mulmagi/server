package shop.mulmagi.app.web.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

public class UmbrellaResponseDto {

    @Builder @Data
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LocationDto{
        private Long locationId;
        private String name;
        private Boolean isRental;
        private List<Integer> umbrellaStandNumber;
    }

    @Builder @Data
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RentalPageDto{
        private Long umbrellaStandId;
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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocationDataListDto {
        private List<LocationDataDto> LocationData;
        private RentalDataDto rentalData;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class LocationDataDto {
            private Long locationId;
            private PointDto point;
        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PointDto {
            private Double latitude;
            private Double longitude;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RentalDataDto {
        private Long rentalId;
        private boolean isOverdue;
        private Integer overdueAmount;
        private LocalDateTime rentalDate;
    }
}
