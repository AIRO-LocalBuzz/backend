package backend.airo.common.exception;

public class NullCheckException extends AiroException {

  public NullCheckException (BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
    }

  public static NullCheckException notFound(BaseErrorCode errorCode, String sourceLayer) {
    return new NullCheckException(errorCode, sourceLayer);
  }
}
