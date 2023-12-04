package shop.mulmagi.app.web.dto;

import lombok.*;

import java.sql.Timestamp;
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

        public PaymentHistoryDto(Object[] objects) {
            this.amount = Integer.valueOf(objects[0].toString());
            this.transactionType = (String) objects[1];
            Timestamp timestamp = (Timestamp) objects[2];
            this.createdAt = timestamp.toLocalDateTime();

        }
    }
}