package shop.mulmagi.app.service.impl;

import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.domain.enums.PaymentMethod;
import shop.mulmagi.app.exception.CustomExceptions;
import shop.mulmagi.app.repository.PaymentRepository;
import shop.mulmagi.app.repository.UserRepository;
import shop.mulmagi.app.service.PaymentService;
import shop.mulmagi.app.web.dto.PaymentResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public void verifyIamportService(IamportResponse<Payment> irsp, User user, Integer amount, PaymentMethod method) throws CustomExceptions.Exception {

        if(irsp.getResponse().getAmount().intValue()!=amount)
            throw new CustomExceptions.Exception("위변조가 의심됩니다.");

        shop.mulmagi.app.domain.Payment payment =
                shop.mulmagi.app.domain.Payment.builder()
                .user(user)
                .method(method)
                .impUid(irsp.getResponse().getImpUid())
                .amount(irsp.getResponse().getAmount().intValue())
                .build();

        paymentRepository.save(payment);

        user.chargePoint(amount);
        userRepository.save(user);
    }

    @Override
    public List<PaymentResponseDto.PaymentHistoryDto> getPaymentHistory(User user){
        List<Object[]> results = paymentRepository.findAmountAndCreatedAtByUserId(user);
        List<PaymentResponseDto.PaymentHistoryDto> paymentHistoryList = results.stream()
                .map(PaymentResponseDto.PaymentHistoryDto::new)
                .collect(Collectors.toList());
        return paymentHistoryList;
    }
}