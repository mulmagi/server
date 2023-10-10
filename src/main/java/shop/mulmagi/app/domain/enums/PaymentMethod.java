package shop.mulmagi.app.domain.enums;

public enum PaymentMethod {
    KAKAOPAY("kakaopay"),
    NAVERPAY("naverpay"),
    APPLEPAY("applepay"),
    TOSS("toss"),
    ETC("etc");

    private final String paymentMethod;

    PaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
