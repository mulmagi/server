package shop.mulmagi.app.exception;

public class CustomExceptions {

    //테스트
    public static class testException extends RuntimeException{
        public testException(String message){
            super(message);
        }
    }

    // Umbrella
    public static class locationException extends RuntimeException{
        public locationException(String message) { super(message); }
    }

    public static class rentalPageException extends RuntimeException{
        public rentalPageException(String message) { super(message); }
    }

    public static class Exception extends RuntimeException {
        public Exception(String message) { super(message);}
    }

}
