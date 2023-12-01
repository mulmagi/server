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
}
