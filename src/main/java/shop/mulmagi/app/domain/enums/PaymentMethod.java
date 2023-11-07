package shop.mulmagi.app.domain.enums;

public enum PaymentMethod {
    KAKAOPAY("카카오페이"),
    TOSSPAY("토스페이"),
    NAVERPAY("네이버페이"),
    APPLEPAY("애플페이"),
    ETC("기타");

    private final String paymentMethod;

    PaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
