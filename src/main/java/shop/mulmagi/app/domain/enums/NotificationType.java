package shop.mulmagi.app.domain.enums;

public enum NotificationType {
    PAYMENT("payment"),
    OVERDUE("overdue"),
    RENTAL("rental"),
    RETURN("return"),
    ETC("etc");
    // REPORT("report")

    private final String notificationType;

    NotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
}
