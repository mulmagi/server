package shop.mulmagi.app.web.controller;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.domain.enums.NotificationType;
import shop.mulmagi.app.exception.ResponseMessage;
import shop.mulmagi.app.exception.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import shop.mulmagi.app.exception.CustomExceptions;
import shop.mulmagi.app.repository.UserRepository;
import shop.mulmagi.app.service.NotificationService;
import shop.mulmagi.app.service.UmbrellaService;
import shop.mulmagi.app.service.UserService;
import shop.mulmagi.app.web.controller.base.BaseController;
import shop.mulmagi.app.web.dto.UmbrellaRequestDto;
import shop.mulmagi.app.web.dto.UmbrellaResponseDto;
import shop.mulmagi.app.web.dto.base.DefaultRes;

@Api(tags = "우산 관리 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UmbrellaController extends BaseController {
    private final UmbrellaService umbrellaService;
    private final NotificationService notificationService;
    private final UserService userService;

    @ApiOperation(value = "메인 화면 불러오기 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
    })
    @ApiResponse(code = 200, message = "메인 화면 불러오기 성공")
    @GetMapping("/main")
    public ResponseEntity main(@RequestParam("latitude") @ApiParam(value = "사용자 위치(지도 중심)의 위도", example = "37.2431") double latitude, @RequestParam("longitude") @ApiParam(value = "사용자 위치(지도 중심)의 경도", example = "127.0736") double longitude){
        try {
            logger.info("Received request: method={}, path={}, description={}", "Get", "/api/main", "메인 화면 불러오기 API");

            User user = userService.getCurrentUser();

            UmbrellaResponseDto.LocationDataListDto res = umbrellaService.getLocationData(user, latitude, longitude);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.MAIN_SCREEN_READ_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "location 불러오기 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
    })
    @ApiResponse(code = 200, message = "location 불러오기 성공")
    @GetMapping("/main/{location-id}")
    public ResponseEntity location(@PathVariable("location-id") @ApiParam(value = "location ID", example = "1") Long locationId){
        try {
            logger.info("Received request: method={}, path={}, description={}", "Get", "/api/main/{location-id}", "location 불러오기 API");

            User user = userService.getCurrentUser();

            UmbrellaResponseDto.LocationDto res = umbrellaService.getLocation(user, locationId);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.LOCATION_READ_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "대여 페이지 불러오기 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
    })
    @ApiResponse(code = 200, message = "대여 페이지 불러오기 성공")
    @GetMapping("/rental")
    public ResponseEntity rentalPage(@RequestParam("qrCode") @ApiParam(value = "QR 코드", example = "123456") Long qrCode){
        try {
            logger.info("Received request: method={}, path={}, description={}", "Get", "/api/rental", "대여 페이지 불러오기 API");

            User user = userService.getCurrentUser();

            UmbrellaResponseDto.RentalPageDto res = umbrellaService.getRentalPage(user, qrCode);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.RENTAL_PAGE_READ_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "반납 완료 페이지 불러오기 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
    })
    @ApiResponse(code = 200, message = "반납 완료 페이지 불러오기 성공")
    @GetMapping("/return/{rental-id}")
    public ResponseEntity returnPage(@PathVariable("rental-id") @ApiParam(value = "rental ID", example = "1") Long rentalId){
        try {
            logger.info("Received request: method={}, path={}, description={}", "Get", "/api/return", "반납 완료 페이지 불러오기 API");

            User user = userService.getCurrentUser();

            UmbrellaResponseDto.ReturnPageDto res = umbrellaService.getReturnPage(rentalId);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.RETURN_PAGE_READ_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "대여하기 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
    })
    @ApiResponse(code = 200, message = "대여 성공")
    @PostMapping("/rental")
    public ResponseEntity rentalUmbrella(@RequestBody UmbrellaRequestDto.RentalDto request){
        try {
            logger.info("Received request: method={}, path={}, description={}", "POST", "/api/rental", "대여하기 API");

            User user = userService.getCurrentUser();

            String notificationMsg = umbrellaService.rental(user, request);

            notificationService.sendAndSaveNotification(user, NotificationType.RENTAL, notificationMsg, "");

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.RENTAL_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "반납하기 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
    })
    @ApiResponse(code = 200, message = "반납 성공")
    @PatchMapping("/return")
    public ResponseEntity returnUmbrella(@RequestBody UmbrellaRequestDto.ReturnDto request){
        try {
            logger.info("Received request: method={}, path={}, description={}", "PATCH", "/api/return", "반납하기 API");

            User user = userService.getCurrentUser();

            String notificationMsg = umbrellaService.returnUmb(user, request);

            notificationService.sendAndSaveNotification(user, NotificationType.RETURN, notificationMsg, "");

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.RETURN_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }
}
