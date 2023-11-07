package shop.mulmagi.app.service;

import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.domain.enums.PaymentMethod;

public interface PaymentService {
    public void verifyIamportService(IamportResponse<Payment> irsp, User user, Integer amount, PaymentMethod method);
}