package shop.mulmagi.app.web.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Getter;


public class NotificationRequestDto {
    @Builder
    @Getter
    public static class FcmRequestDto{

        @ApiModelProperty(example = "1")
        @ApiParam(name = "targetUserId", value = "알림을 보낼 유저의 ID 입력")
        private Long targetUserId;

        @ApiModelProperty(example = "1,000포인트 충전 완료")
        @ApiParam(name = "title", value = "메인 내용 입력")
        private String title;

        @ApiModelProperty(example = "1,000포인트 > 2,000포인트")
        @ApiParam(name = "body", value = "세부 내용 입력")
        private String body;
    }
}
