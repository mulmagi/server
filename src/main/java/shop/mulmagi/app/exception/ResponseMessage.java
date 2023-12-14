package shop.mulmagi.app.exception;

public class ResponseMessage {

    //umbrella
    public static final String MAIN_SCREEN_READ_SUCCESS = "메인 화면 불러오기 성공";
    public static final String LOCATION_READ_SUCCESS = "location 불러오기 성공";
    public static final String RENTAL_PAGE_READ_SUCCESS = "대여 페이지 불러오기 성공";
    public static final String RETURN_PAGE_READ_SUCCESS = "반납 완료 페이지 불러오기 성공";
    public static final String RENTAL_SUCCESS = "대여 성공";
    public static final String RETURN_SUCCESS = "반납 성공";

    //payment
    public static final String PAYMENT_SUCCESS = "결제 성공";
    public static final String PAYMENT_HISTORY_READ_SUCCESS = "포인트 내역 불러오기 성공";
  
    //notification
    public static final String NOTIFICATION_HISTORY_ALL_READ_SUCCESS = "전체 알림 내역 불러오기 성공";
    public static final String NOTIFICATION_HISTORY_RENTAL_READ_SUCCESS = "대여/반납/연체 알림 내역 불러오기 성공";
    public static final String NOTIFICATION_HISTORY_POINT_READ_SUCCESS = "포인트 관련 알림 내역 불러오기 성공";
    public static final String NOTIFICATION_HISTORY_ETC_READ_SUCCESS = "기타 알림 내역 불러오기 성공";
    public static final String NOTIFICATION_ALLOW_SUCCESS = "푸시 알림 허용하기 성공";
    public static final String NOTIFICATION_DENY_SUCCESS = "푸시 알림 거부하기 성공";

    //notice
    public static final String NOTICE_READ_SUCCESS = "공지사항 불러오기 성공";
    public static final String NOTICE_DETAIL_READ_SUCCESS = "공지사항 세부내용 불러오기 성공";
    public static final String NOTICE_CATEGORY_READ_SUCCESS = "공지사항 카테고리 불러오기 성공";
    public static final String NOTICE_UPLOAD_SUCCESS = "공지사항 업로드 성공";
    public static final String NOTICE_DELETE_SUCCESS = "공지사항 삭제 성공";
    public static final String NOTICE_UPDATE_SUCCESS = "공지사항 수정 성공";

    //report
    public static final String REPORT_RECEIVE_SUCCESS = "고장신고 접수 성공";
    public static final String REPORT_SOLVE_SUCCESS = "고장신고 해결처리 성공";

    //sms certification
    public static final String SMS_CERT_SUCCESS = "sms 본인 확인 성공";
    public static final String SMS_CERT_MESSAGE_SUCCESS = "인증 문자 전송 성공";

    //chat
    public static final String CHATROOM_READ_SUCCESS = "모든 채팅방 불러오기 성공";
    public static final String CHATROOM_CREAT_SUCCESS = "채팅방 생성 성공";
    public static final String CHATROOM_DELETE_SUCCESS = "채팅방 삭제 성공";
    public static final String MESSAGE_READ_SUCCESS = "메시지 목록 불러오기 성공";

    // user
    public static final String NAME_SUBMIT_SUCCESS = "이름 입력 성공";
    public static final String USER_LOGIN_SUCCESS = "로그인 성공";
    public static final String USER_LOGOUT_SUCCESS = "로그아웃 성공";
    public static final String REFRESH_TOKEN_ISSUE_SUCCESS = "Refresh Token 발급 완료";
    public static final String ACCESS_TOKEN_ISSUE_SUCCESS = "Access Token 발급 완료";
    public static final String ACCESS_TOKEN_REISSUE_SUCCESS = "Access Token 재발급 완료";
    public static final String USER_DELETION_SUCCESS = "회원 탈퇴 완료";

    public static final String TOKEN_VALIDATE_SUCCESS = "Token 인증 완료";

    public static final String PROFILE_IMAGE_UPLOAD_SUCCESS = "프로필 이미지 업로드 성공";
    public static final String PRINT_RENTAL_HISTORY_SUCCESS = "반납 및 대여 내역 조회 성공";

    public static final String USER_AGREED_NOTIFICATION = "알람 수신 동의";
    public static final String USER_DECLINE_NOTIFICATION = "알람 수신 미동의";
    public static final String ACCESS_TOKEN_SEND_SUCCESS = "Access token을 클라이언트에게 줌";
}
