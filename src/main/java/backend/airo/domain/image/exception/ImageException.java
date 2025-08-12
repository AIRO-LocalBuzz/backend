package backend.airo.domain.image.exception;

import backend.airo.common.exception.AiroException;
import backend.airo.common.exception.BaseErrorCode;
import java.util.List;

public class ImageException extends AiroException {

    public ImageException(BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
    }

    // UnsupportedFormatException을 대체하는 정적 팩토리 메서드
    public static ImageException unsupportedFormat(String mimeType, String[] supportedFormats) {
        return new UnsupportedFormatImageException(
                ImageErrorCode.IMAGE_UNSUPPORTED_FORMAT,
                "DOMAIN",
                mimeType,
                supportedFormats
        );
    }

    public static ImageException unsupportedFormat(String mimeType, String[] supportedFormats, String layer) {
        return new UnsupportedFormatImageException(
                ImageErrorCode.IMAGE_UNSUPPORTED_FORMAT,
                layer,
                mimeType,
                supportedFormats
        );
    }

    // ImageNotFoundException을 대체하는 정적 팩토리 메서드
    public static ImageException notFound(Long imageId) {
        return new NotFoundImageException(
                ImageErrorCode.IMAGE_NOT_FOUND,
                "DOMAIN",
                imageId,
                null
        );
    }

    public static ImageException notFound(List<Long> imageIds) {
        return new NotFoundImageException(
                ImageErrorCode.IMAGE_NOT_FOUND,
                "DOMAIN",
                null,
                imageIds
        );
    }

    // UnauthorizedException을 대체하는 정적 팩토리 메서드
    public static ImageException unauthorized(String operation) {
        return new UnauthorizedImageException(
                determineErrorCode(operation),
                "DOMAIN",
                operation
        );
    }

    public static ImageException unauthorized(String operation, String layer) {
        return new UnauthorizedImageException(
                determineErrorCode(operation),
                layer,
                operation
        );
    }

    private static BaseErrorCode determineErrorCode(String operation) {
        if (operation.contains("게시물")) {
            return ImageErrorCode.POST_IMAGES_DELETE_UNAUTHORIZED;
        }
        return ImageErrorCode.IMAGE_DELETE_UNAUTHORIZED;
    }

    // 내부 클래스들
    private static class UnsupportedFormatImageException extends ImageException {
        private final String mimeType;
        private final String[] supportedFormats;

        public UnsupportedFormatImageException(BaseErrorCode errorCode, String sourceLayer,
                                               String mimeType, String[] supportedFormats) {
            super(errorCode, sourceLayer);
            this.mimeType = mimeType;
            this.supportedFormats = supportedFormats;
        }

        @Override
        public String getMessage() {
            String baseMessage = super.getMessage();
            return String.format("%s - 지원하지 않는 형식: %s, 지원 형식: %s",
                    baseMessage, mimeType, String.join(", ", supportedFormats));
        }
    }

    private static class NotFoundImageException extends ImageException {
        private final Long imageId;
        private final List<Long> imageIds;

        public NotFoundImageException(BaseErrorCode errorCode, String sourceLayer,
                                      Long imageId, List<Long> imageIds) {
            super(errorCode, sourceLayer);
            this.imageId = imageId;
            this.imageIds = imageIds;
        }

        @Override
        public String getMessage() {
            String baseMessage = super.getMessage();
            if (imageId != null) {
                return String.format("%s - 이미지 ID: %d", baseMessage, imageId);
            } else if (imageIds != null) {
                return String.format("%s - 이미지 IDs: %s", baseMessage, imageIds.toString());
            }
            return baseMessage;
        }
    }

    private static class UnauthorizedImageException extends ImageException {
        private final String operation;

        public UnauthorizedImageException(BaseErrorCode errorCode, String sourceLayer,
                                          String operation) {
            super(errorCode, sourceLayer);
            this.operation = operation;
        }

        @Override
        public String getMessage() {
            String baseMessage = super.getMessage();
            return String.format("%s - %s 권한이 없습니다", baseMessage, operation);
        }
    }
}