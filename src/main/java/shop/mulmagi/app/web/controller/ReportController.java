package shop.mulmagi.app.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import shop.mulmagi.app.domain.UmbrellaStand;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.exception.CustomExceptions;
import shop.mulmagi.app.exception.ResponseMessage;
import shop.mulmagi.app.exception.StatusCode;
import shop.mulmagi.app.repository.UmbrellaStandRepository;
import shop.mulmagi.app.service.impl.ReportServiceImpl;
import shop.mulmagi.app.web.controller.base.BaseController;
import shop.mulmagi.app.web.dto.UmbrellaResponseDto;
import shop.mulmagi.app.web.dto.base.DefaultRes;

@Api(tags = "고장신고 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController extends BaseController {
	ReportServiceImpl reportService;

	@ApiOperation(value = "우산 고장신고 접수 API")
	@PutMapping("/receive/{id}")
	public ResponseEntity receiveReport(@PathVariable("id") Long id){
		try {
			logger.info("Received request: method={}, path={}, description={}", "Put", "/api/report/receive/{id}", "우산 고장신고 접수 API");

			reportService.receiveReport(id);

			return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.REPORT_RECEIVE_SUCCESS), HttpStatus.OK);
		} catch (CustomExceptions.Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "우산 고장신고 해결처리 API")
	@PutMapping("/solve/{id}")
	public ResponseEntity solveReport(@PathVariable("id") Long id){
		try {
			logger.info("Received request: method={}, path={}, description={}", "Put", "/api/report/solve/{id}", "우산 고장신고 해결처리 API");

			reportService.solveReport(id);

			return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.REPORT_SOLVE_SUCCESS), HttpStatus.OK);
		} catch (CustomExceptions.Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}
}
