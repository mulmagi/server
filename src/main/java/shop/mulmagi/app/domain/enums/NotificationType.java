package shop.mulmagi.app.domain.enums;

import lombok.Getter;

@Getter
public enum NotificationType {
    PAYMENT("결제"),
    OVERDUE("연체"),
    RENTAL("대여"),
    RETURN("반납"),
    ETC("기타");
    // REPORT("신고")

    private final String notificationType;

    NotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
}
