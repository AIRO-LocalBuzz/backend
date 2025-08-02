package backend.airo.domain.image.exception;

public class UnsupportedFormatException extends RuntimeException {

//    public UnsupportedFormatException(String message) {
//        super(message);
//    }

    public UnsupportedFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedFormatException(String mimeType) {
        super("지원하지 않는 파일 형식입니다: " + mimeType);
    }

    public UnsupportedFormatException(String mimeType, String[] supportedTypes) {
        super(String.format("지원하지 않는 파일 형식입니다: %s. 지원되는 형식: %s",
                mimeType, String.join(", ", supportedTypes)));
    }
}