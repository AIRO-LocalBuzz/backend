package backend.airo.api.global.aop;

import backend.airo.api.global.dto.Response;
import backend.airo.common.exception.AiroException;
import org.springframework.cache.Cache;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalControllerErrorHandler {

    //AiroException을 상속받은 Exception Handler
    @ExceptionHandler(AiroException.class)
    public ResponseEntity<Response<Void>> handleCustomException(AiroException exception) {
        return ResponseEntity
                .status(exception.getErrorCode().getErrorReason().status())
                .body(Response.error(exception.getErrorCode().toString(), exception.getMessage()));
    }

    @ExceptionHandler(org.springframework.cache.Cache.ValueRetrievalException.class)
    public ResponseEntity<?> handleCacheLoad(Cache.ValueRetrievalException e) {
        Throwable root = (e.getCause() != null) ? e.getCause() : e;
        return ResponseEntity.status(500).body(Map.of(
                "error", "CACHE_LOAD_FAILED",
                "message", root.getMessage()
        ));
    }

    //전역 Global Error Handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> globalExceptionHandler(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "STSTEN ERROR :: "+ exception.getMessage()));
    }
}
