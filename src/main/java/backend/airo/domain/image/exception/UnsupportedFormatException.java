package backend.airo.domain.image.exception;

import backend.airo.common.exception.BaseErrorCode;

/**
 * 지원하지 않는 이미지 형식일 때 발생하는 예외
 */
public class UnsupportedFormatException extends ImageException {

    private final String mimeType;
    private final String[] supportedFormats;

    public UnsupportedFormatException(String mimeType, String[] supportedFormats) {
        super(ImageErrorCode.IMAGE_UNSUPPORTED_FORMAT, "DOMAIN");
        this.mimeType = mimeType;
        this.supportedFormats = supportedFormats;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String[] getSupportedFormats() {
        return supportedFormats;
    }

    @Override
    public String getMessage() {
        return String.format("%s - 지원하지 않는 형식: %s, 지원 형식: %s",
                sourceLayer, mimeType, String.join(", ", supportedFormats));
    }
}