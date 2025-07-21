package backend.airo.api.global.aop;

import backend.airo.api.global.dto.Response;
import backend.airo.common.exception.AiroException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerErrorHandler {

    //AiroException을 상속받은 Exception Handler
    @ExceptionHandler(AiroException.class)
    public ResponseEntity<Response<Void>> handleCustomException(AiroException exception) {
        return ResponseEntity
                .status(exception.getErrorCode().getErrorReason().status())
                .body(Response.error(exception.getErrorCode().toString(), exception.getMessage()));
    }

    //Bean Validation 에러 핸들러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Void>> handleValidationException(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("잘못된 입력값입니다.");
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.error("VALIDATION_ERROR", errorMessage));
    }

    //전역 Global Error Handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> globalExceptionHandler(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "STSTEN ERROR :: "+ exception.getMessage()));
    }
}
