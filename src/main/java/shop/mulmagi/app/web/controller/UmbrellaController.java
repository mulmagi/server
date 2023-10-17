package shop.mulmagi.app.web.controller;

import org.springframework.web.bind.annotation.*;
import shop.mulmagi.app.converter.TestConverter;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.exception.ResponseMessage;
import shop.mulmagi.app.exception.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import shop.mulmagi.app.domain.Test;
import shop.mulmagi.app.exception.CustomExceptions;
import shop.mulmagi.app.repository.UserRepository;
import shop.mulmagi.app.service.TestService;
import shop.mulmagi.app.service.impl.UmbrellaServiceImpl;
import shop.mulmagi.app.web.controller.base.BaseController;
import shop.mulmagi.app.web.dto.TestRequestDto;
import shop.mulmagi.app.web.dto.TestResponseDto;
import shop.mulmagi.app.web.dto.UmbrellaRequestDto;
import shop.mulmagi.app.web.dto.UmbrellaResponseDto;
import shop.mulmagi.app.web.dto.base.DefaultRes;

@Api(tags = "우산 관리 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UmbrellaController extends BaseController {

    private final UserRepository userRepository;
    private final UmbrellaServiceImpl umbrellaService;

    @ApiOperation(value = "location 불러오기 API")
    @ApiResponse(code = 200, message = "location 불러오기  성공")
    @GetMapping("/main/{location-id}")
    public ResponseEntity location(@PathVariable("location-id") Long locationId){
        try {
            logger.info("Received request: method={}, path={}, description={}", "Get", "/api/main/{location-id}", "location 불러오기 API");

            //로그인 구현 후 유저 정보 토큰으로 받아올 예정
            User user = userRepository.findByPhoneNumber("01029440386");

            UmbrellaResponseDto.LocationDto res = umbrellaService.getLocation(locationId);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.LOCATION_READ_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.locationException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "대여 페이지 불러오기 API")
    @ApiResponse(code = 200, message = "대여 페이지 불러오기  성공")
    @GetMapping("/rental")
    public ResponseEntity rentalPage(@RequestBody UmbrellaRequestDto.RentalPageDto request){
        try {
            logger.info("Received request: method={}, path={}, description={}", "Get", "/api/rental", "대여 페이지 불러오기 API");

            //로그인 구현 후 유저 정보 토큰으로 받아올 예정
            User user = userRepository.findByPhoneNumber("01029440386");

            UmbrellaResponseDto.RentalPageDto res = umbrellaService.getRentalPage(user, request);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.RENTAL_PAGE_READ_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.rentalPageException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "반납 완료 페이지 불러오기 API")
    @ApiResponse(code = 200, message = "반납 완료 페이지 불러오기  성공")
    @GetMapping("/return")
    public ResponseEntity returnPage(@RequestBody UmbrellaRequestDto.ReturnPageDto request){
        try {
            logger.info("Received request: method={}, path={}, description={}", "Get", "/api/return", "반납 완료 페이지 불러오기 API");

            //로그인 구현 후 유저 정보 토큰으로 받아올 예정
            User user = userRepository.findByPhoneNumber("01029440386");

            UmbrellaResponseDto.ReturnPageDto res = umbrellaService.getReturnPage(request);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.RETURN_PAGE_READ_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.rentalPageException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "대여하기 API")
    @ApiResponse(code = 200, message = "대여하기 API 성공")
    @PostMapping("/rental")
    public ResponseEntity rental(@RequestBody UmbrellaRequestDto.RentalDto request){
        try {
            logger.info("Received request: method={}, path={}, description={}", "POST", "/api/rental", "대여하기 API");

            //로그인 구현 후 유저 정보 토큰으로 받아올 예정
            User user = userRepository.findByPhoneNumber("01029440386");

            String res = umbrellaService.rental(user, request);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.RENTAL_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.rentalPageException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }
}
