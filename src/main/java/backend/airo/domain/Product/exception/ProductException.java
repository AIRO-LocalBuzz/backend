package backend.airo.domain.Product.exception;

import backend.airo.common.exception.AiroException;
import backend.airo.common.exception.BaseErrorCode;

public class ProductException extends AiroException {

    private ProductException(BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
    }

    public static ProductException notFound(BaseErrorCode errorCode, String sourceLayer) {
        return new ProductException(errorCode, sourceLayer);
    }
}