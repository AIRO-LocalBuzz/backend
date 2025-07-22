package backend.airo.domain.shop.exception;

import backend.airo.common.exception.AiroException;
import backend.airo.common.exception.BaseErrorCode;

public class ShopException extends AiroException {

    private ShopException(BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
    }

    public static ShopException notFound(BaseErrorCode errorCode, String sourceLayer) {
        return new ShopException(errorCode, sourceLayer);
    }
}