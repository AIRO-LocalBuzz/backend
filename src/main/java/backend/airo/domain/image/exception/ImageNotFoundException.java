package backend.airo.domain.image.exception;

public class ImageNotFoundException extends RuntimeException {

    public ImageNotFoundException(String message) {
        super(message);
    }

    public ImageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageNotFoundException(Long imageId) {
        super("이미지를 찾을 수 없습니다: " + imageId);
    }

    public ImageNotFoundException(String field, Object value) {
        super(String.format("이미지를 찾을 수 없습니다. %s: %s", field, value));
    }
}