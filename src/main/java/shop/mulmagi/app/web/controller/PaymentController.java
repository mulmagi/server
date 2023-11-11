package shop.mulmagi.app.web.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.domain.enums.PaymentMethod;
import shop.mulmagi.app.exception.CustomExceptions;
import shop.mulmagi.app.exception.ResponseMessage;
import shop.mulmagi.app.exception.StatusCode;
import shop.mulmagi.app.repository.UserRepository;
import shop.mulmagi.app.service.impl.PaymentServiceImpl;
import shop.mulmagi.app.web.controller.base.BaseController;
import shop.mulmagi.app.web.dto.PaymentRequestDto;
import shop.mulmagi.app.web.dto.base.DefaultRes;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Api(tags = "포인트 결제 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PaymentController extends BaseController {
    @Value("${iamport.key}")
    private String restApiKey;
    @Value("${iamport.secret}")
    private String restApiSecret;
    private IamportClient iamportClient;
    @Autowired
    private PaymentServiceImpl paymentService;
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        iamportClient = new IamportClient(restApiKey, restApiSecret);
    }

    public IamportResponse<Payment> paymentLookup(String impUid) throws IamportResponseException, IOException{
        return iamportClient.paymentByImpUid(impUid);
    }

    @ApiOperation(value = "결제하기 API")
    @ApiResponse(code = 200, message = "결제 성공")
    @PostMapping("/verifyIamport")
    @CrossOrigin("*") // 임시 허용
    public ResponseEntity verifyIamport(@RequestBody PaymentRequestDto.VerifyIamportDto request) throws IamportResponseException, IOException {
        try {
            logger.info("Received request: method={}, path={}, description={}", "Post", "/api/verifyIamport", "결제하기 API");

            String impUid = request.getImpUid();
            Integer amount = request.getAmount();
            PaymentMethod method = PaymentMethod.valueOf(request.getMethod());

            logger.info("Received Data: impUid={}, amount={}, method={}", impUid, amount, method);

            User user = userRepository.findByPhoneNumber("01043939869");

            IamportResponse<Payment> irsp = paymentLookup(impUid);
            paymentService.verifyIamportService(irsp, user, amount, method);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.PAYMENT_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }
}
