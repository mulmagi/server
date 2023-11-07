package shop.mulmagi.app.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;

public class PaymentRequestDto {
    @Getter
    public static class VerifyIamportDto{
        @JsonProperty("imp_uid")
        @ApiParam(name = "imp_uid", value = "impUid 입력", required = true)
        private String impUid;
        @ApiParam(name = "amount", value = "amount 입력", required = true)
        private Integer amount;
        @ApiParam(name = "mehod", value = "method 입력", required = true)
        private String method;
    }
}
