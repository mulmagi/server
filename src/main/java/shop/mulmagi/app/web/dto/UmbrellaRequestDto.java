package shop.mulmagi.app.web.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;

public class UmbrellaRequestDto {

    @Getter
    public static class RentalPageDto{
        @ApiModelProperty(example = "123225")
        @ApiParam(name = "QR code", value = "QR 코드 입력", required = true)
        private Long qrCode;
    }

    @Getter
    public static class ReturnPageDto{
        @ApiModelProperty(example = "1")
        @ApiParam(name = "rentalId", value = "rental ID 입력", required = true)
        private Long rentalId;
    }
}
