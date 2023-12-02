package shop.mulmagi.app.web.dto;

import lombok.*;

import java.time.LocalDateTime;

public class NotificationResponseDto {
    @Builder
    @Data
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class NotificationHistoryDto{
        private String type;
        private String title;
        private String body;
        private LocalDateTime createdAt;
    }
}
