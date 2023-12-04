package shop.mulmagi.app.web.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;

public class UmbrellaRequestDto {
    @Getter
    public static class RentalDto{
        @ApiModelProperty(example = "1")
        @ApiParam(name = "umbrellaStandId", value = "Umbrella Stand ID 입력", required = true)
        private Long umbrellaStandId;
    }

    @Getter
    public static class ReturnDto {
        @ApiModelProperty(example = "345678")
        @ApiParam(name = "QR code", value = "QR 코드 입력", required = true)
        private Long qrCode;

        @ApiModelProperty(example = "1")
        @ApiParam(name = "Rental ID", value = "Rental ID 입력", required = true)
        private Long rentalId;
    }
}
