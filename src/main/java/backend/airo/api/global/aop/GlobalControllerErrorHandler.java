// java
package backend.airo.api.global.aop;

import backend.airo.api.global.dto.Response;
import backend.airo.common.exception.*;
import backend.airo.api.global.config.ValidationErrorMappingConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerErrorHandler {

    private final ValidationErrorMappingConfig errorMappingConfig;

    @ExceptionHandler(AiroException.class)
    public ResponseEntity<Response<Void>> handleAiroException(AiroException exception) {
        return ResponseEntity
                .status(exception.getErrorCode().getErrorReason().status())
                .body(Response.error(
                        exception.getErrorCode().toString(),
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Response<Void>> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Response.error("ACCESS_DENIED", "인증 실패 또는 권한 없음"));
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Response<Void>> handleConstraintViolationException(
            ConstraintViolationException ex, HttpServletRequest request) {
        ConstraintViolation<?> violation = ex.getConstraintViolations().iterator().next();
        return handleAiroException(new AiroException(RequestErrorCode.INVALID_INPUT, violation.getMessage()));
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Void>> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorMessage = ex.getBindingResult().getFieldError() != null
                ? ex.getBindingResult().getFieldError().getDefaultMessage()
                : ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.error(RequestErrorCode.INVALID_INPUT.getErrorReason(), request.getRequestURI(), errorMessage));
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Response<Void>> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return handleAiroException(new AiroException(RequestErrorCode.INVALID_INPUT, RequestErrorCode.INVALID_INPUT.getErrorReason().message()));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> globalExceptionHandler(Exception exception) {
        log.error("예상치 못한 오류 발생", exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "STSTEN ERROR :: " + exception.getMessage()));
    }
}