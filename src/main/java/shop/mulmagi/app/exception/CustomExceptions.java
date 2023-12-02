package shop.mulmagi.app.exception;

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



}
