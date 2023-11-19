package shop.mulmagi.app.web.dto;

import lombok.*;

import java.time.LocalDateTime;

public class PaymentResponseDto {

    @Builder
    @Data
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PaymentHistoryDto{
        private Integer amount;
        private String transactionType;
        private LocalDateTime createdAt;
    }
}