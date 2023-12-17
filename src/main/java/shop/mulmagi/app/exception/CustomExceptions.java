package shop.mulmagi.app.exception;

import shop.mulmagi.app.domain.Rental;

import java.util.List;

public class CustomExceptions {
    public static class Exception extends RuntimeException {
        public Exception(String message) { super(message);}
    }

    public static class SmsCertificationNumberMismatchException extends RuntimeException{
        public SmsCertificationNumberMismatchException(String message){super(message);}
    }

    public static class MessageNotSentException extends RuntimeException {
        public MessageNotSentException(String message) { super(message);}
    }

    public static class UserPhoneNumberNotFoundException extends  RuntimeException{
        public UserPhoneNumberNotFoundException(String message){super(message);}
    }

    public static class RefreshTokenInvalidException extends  RuntimeException{
        public RefreshTokenInvalidException(String message){super(message);}
    }

    public static class UserNotActiveException extends RuntimeException{
        public UserNotActiveException(String message){super(message);}
    }
    public static class UserNotFoundException extends RuntimeException{
        public UserNotFoundException(String message){super(message);}
    }
    public static class NoRentalHistoryFoundException extends RuntimeException{
        public NoRentalHistoryFoundException(String message){super(message);}
    }




}
