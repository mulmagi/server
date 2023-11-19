package shop.mulmagi.app.web.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;

public class UmbrellaRequestDto {
    @Getter
    public static class LocationPointDto{
        @ApiModelProperty(example = "37.2431")
        @ApiParam(name = "latitude", value = "화면 중앙(user 위치)의 위도", required = true)
        private Double latitude;

        @ApiModelProperty(example = "127.0736")
        @ApiParam(name = "longitude", value = "화면 중앙(user 위치)의 경도", required = true)
        private Double longitude;
    }

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

    @Getter
    public static class RentalDto{
        @ApiModelProperty(example = "1")
        @ApiParam(name = "umbrellaStandId", value = "Umbrella Stand ID 입력", required = true)
        private Long umbrellaStandId;
    }

    @Getter
    public static class ReturnDto {
        @ApiModelProperty(example = "123225")
        @ApiParam(name = "QR code", value = "QR 코드 입력", required = true)
        private Long qrCode;

        @ApiModelProperty(example = "1")
        @ApiParam(name = "Rental ID", value = "Rental ID 입력", required = true)
        private Long rentalId;
    }
}
