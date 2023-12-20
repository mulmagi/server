package shop.mulmagi.app.web.controller;

import java.io.IOException;
import java.util.List;

import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.domain.enums.AnnouncementCategory;
import shop.mulmagi.app.exception.CustomExceptions;
import shop.mulmagi.app.exception.ResponseMessage;
import shop.mulmagi.app.exception.StatusCode;
import shop.mulmagi.app.service.AnnouncementService;
import shop.mulmagi.app.service.S3UploadService;
import shop.mulmagi.app.service.UserService;
import shop.mulmagi.app.web.controller.base.BaseController;
import shop.mulmagi.app.web.dto.AnnouncementRequestDto;
import shop.mulmagi.app.web.dto.AnnouncementResponseDto;
import shop.mulmagi.app.web.dto.base.DefaultRes;

@Api(tags = "공지사항 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/announcement")
public class AnnouncementController extends BaseController {

	private final AnnouncementService announcementService;
	private final S3UploadService s3UploadService;
	private final UserService userService;

	@ApiOperation(value = "공지사항 불러오기 API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
	})
	@ApiResponse(code = 200, message = "공지사항 불러오기 성공")
	@GetMapping
	public ResponseEntity announcement(){
		try {
			logger.info("Received request: method={}, path={}, description={}", "Get", "/announcement", "전체 공지사항 불러오기");
			//id,날짜,제목,분류만 간략히
			List<AnnouncementResponseDto.AnnouncementPageDto> res = announcementService.getAnnouncements();

			return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.NOTICE_READ_SUCCESS, res), HttpStatus.OK);
		} catch (CustomExceptions.Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "공지사항 세부내용 불러오기 API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
	})
	@ApiResponse(code = 200, message = "공지사항 세부내용 불러오기 성공")
	@ApiImplicitParam(name = "id", value = "불러올 공지사항 ID", example = "1")
	@GetMapping("/{id}")
	public ResponseEntity announcementDetail(@PathVariable Long id){
		try {
			logger.info("Received request: method={}, path={}, description={}", "Get", "/announcement/{id}", "공지사항 세부내용 불러오기");
			//자세한 내용
			AnnouncementResponseDto.AnnouncementDetailDto res = announcementService.getAnnouncementDetail(id);

			return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.NOTICE_DETAIL_READ_SUCCESS, res), HttpStatus.OK);
		} catch (CustomExceptions.Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "특정 카테고리 공지사항 불러오기 API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
	})
	@ApiResponse(code = 200, message = "공지사항 카테고리 불러오기 성공")
	@ApiImplicitParam(name = "category", value = "선택한 특정 공지사항 category(NORMAL/CHARGE/EVENT/SYSTEM/ETC)", example = "NORMAL")
	@GetMapping("/category/{category}")
	public ResponseEntity announcementCategory(@PathVariable("category") String categoryValue){
		try {
			logger.info("Received request: method={}, path={}, description={}", "Get", "/announcement/{category}", "특정 카테고리 공지사항 불러오기");

			AnnouncementCategory category = AnnouncementCategory.valueOf(categoryValue);
			List<AnnouncementResponseDto.AnnouncementPageDto> res = announcementService.getAnnouncementCategory(category);

			return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.NOTICE_CATEGORY_READ_SUCCESS, res), HttpStatus.OK);
		}  catch (CustomExceptions.Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "공지사항 업로드 API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
	})
	@ApiResponse(code = 200, message = "공지사항 업로드 성공")
	@PostMapping("/add")
	public ResponseEntity addAnnouncement(@ModelAttribute AnnouncementRequestDto.UploadDto request) throws IOException {
		logger.info("Received request: method={}, path={}, description={}", "Post", "/api/announcement/add", "공지사항 업로드");
		try{
			User user = userService.getCurrentUser();

			logger.info("=========================================================================");
			logger.info("[title] " + request.getTitle());
			logger.info("[category] " + request.getCategory());
			logger.info("[content] " + request.getContent());
			if(request.getPhoto() != null && !request.getPhoto().isEmpty()){
				logger.info("[photo] " + request.getPhoto().getOriginalFilename());
				logger.info("	[size] " + request.getPhoto().getSize());
			}
			if(request.getFile_0() != null && !request.getFile_0().isEmpty()) {
				logger.info("[file] " + request.getFile_0().getOriginalFilename());
				logger.info("	[size] " + request.getFile_0().getSize());
			}
			logger.info("=========================================================================");

			//공지사항당 최대 이미지 1개, 파일 1개. 여러 파일추가시 추가 entity 필요?
			//admin-page 파일 1개까지만 첨부한다는 가정하에 작성했습니다
			String title = request.getTitle();
			String content = request.getContent();
			AnnouncementCategory category = request.getCategory();
			MultipartFile image = request.getPhoto();
			MultipartFile file = request.getFile_0();
			String imgUrl = null;
			String fileUrl = null;
			if(image != null && !image.isEmpty()){
				imgUrl = s3UploadService.uploadAWS(image);
			}
			if(file != null && !file.isEmpty()){
				fileUrl = s3UploadService.uploadAWS(file);
			}
			announcementService.upload(title, category, content, imgUrl, fileUrl);

			return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.NOTICE_UPLOAD_SUCCESS), HttpStatus.OK);
		} catch (CustomExceptions.Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "공지사항 삭제 API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
	})
	@ApiResponse(code = 200, message = "공지사항 삭제 성공")
	@ApiImplicitParam(name = "id", value = "삭제할 공지사항 ID", example = "1")
	@DeleteMapping("/{id}")
	public ResponseEntity deleteAnnouncement(@PathVariable Long id) {
		try {
			User user = userService.getCurrentUser();
			announcementService.deleteAnnouncement(id);
			return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.NOTICE_DELETE_SUCCESS), HttpStatus.OK);
		} catch (CustomExceptions.Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "공지사항 수정 API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "인증 토큰", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer accessToken")
	})
	@ApiResponse(code = 200, message = "공지사항 수정 성공")
	@ApiImplicitParam(name = "id", value = "수정할 공지사항 ID", example = "1")
	@PutMapping("/{id}")
	public ResponseEntity updateAnnouncement(@PathVariable Long id, @ModelAttribute AnnouncementRequestDto.UpdateDto request) {
		try {
			User user = userService.getCurrentUser();

			String title = request.getTitle();
			String content = request.getContent();
			AnnouncementCategory category = request.getCategory();
			MultipartFile image = request.getPhoto();
			MultipartFile file = request.getFile_0();
			String imgUrl = null;
			String fileUrl = null;
			if(image != null && !image.isEmpty()){ //나중에 서비스 층에서 처리하도록 수정
				imgUrl = s3UploadService.uploadAWS(image);
			}
			if(file != null && !file.isEmpty()){
				fileUrl = s3UploadService.uploadAWS(file);
			}
			announcementService.updateAnnouncement(id, title, category, content, imgUrl, fileUrl);

			return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.NOTICE_UPDATE_SUCCESS), HttpStatus.OK);
		}  catch (CustomExceptions.Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}
}
