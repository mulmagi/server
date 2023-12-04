package shop.mulmagi.app.domain.enums;

import lombok.Getter;

@Getter
public enum NotificationType {

    // 대여/반납/연체 알림
    OVERDUE("연체"),
    RENTAL("대여"),
    RETURN("반납"),

    // 포인트 관련 알림
    PAYMENT("포인트 충전"),
    REFUND_POINT("포인트 반환"),
    USE_POINT("포인트 사용"),

    // 기타 알림
    ANNOUNCE("공지"),
    REPORT("신고"),
    ETC("기타");

    private final String notificationType;

    NotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
}
