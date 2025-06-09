package ar.com.mylback.utils;

public class MylException extends Exception {
    private final Type type;
    private final String details;

    public MylException(Type type, String details) {
        super(type.getMessage());
        this.type = type;
        this.details = details;
    }

    public MylException(Type type) {
        this.type = type;
        this.details = "";
    }

    public MylException() {
        this.type = Type.GENERIC_ERROR;
        this.details = "";
    }

    public int getCode() {
        return type.getCode();
    }

    public String getDetails() {
        return details;
    }

    public enum Type {
        GENERIC_ERROR(1000, "Generic error"),
        INIT_DB(1001, "Error init DB"),
        ERROR_DB_TRANSACTION(1002, "Database transaction error"),
        ERROR_LOAD_IMAGE(1003, "Error load image"),
        NULL_PARAMETER(1004, "Null parameter"),
        EMPTY_PARAMETER(1005, "Empty parameter"),
        FIREBASE_ERROR(1006, "Firebase error"),
        HTTP_ERROR_SEND_RESPONSE(1007, "HTTP error send response"),
        NOT_FOUND(1008, "Not found");

        private final int code;
        private final String message;

        Type(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
