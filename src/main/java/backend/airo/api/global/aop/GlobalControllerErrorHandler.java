package backend.airo.api.global.aop;

import backend.airo.api.global.dto.Response;
import backend.airo.common.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerErrorHandler {

    @ExceptionHandler(AiroException.class)
    public Response<Void> handleAiroException(AiroException exception, HttpServletRequest request) {
        return Response.error(
                exception.getErrorCode().getErrorReason(),
                request.getRequestURI(),
                exception.getMessage()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Response<Void> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        return Response.error("ACCESS_DENIED", "인증 실패 또는 권한 없음");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Response<Void> handleConstraintViolationException(
            ConstraintViolationException ex, HttpServletRequest request) {
        ConstraintViolation<?> violation = ex.getConstraintViolations().iterator().next();
        return Response.error(
                RequestErrorCode.INVALID_INPUT.getErrorReason(),
                request.getRequestURI(),
                violation.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<Void> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorMessage = ex.getBindingResult().getFieldError() != null
                ? ex.getBindingResult().getFieldError().getDefaultMessage()
                : ex.getMessage();
        return Response.error(
                RequestErrorCode.INVALID_INPUT.getErrorReason(),
                request.getRequestURI(),
                errorMessage
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Response<Void> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return Response.error(
                RequestErrorCode.INVALID_INPUT.getErrorReason(),
                request.getRequestURI(),
                RequestErrorCode.INVALID_INPUT.getErrorReason().message()
        );
    }

    @ExceptionHandler(Exception.class)
    public Response<Void> globalExceptionHandler(Exception exception) {
        log.error("예상치 못한 오류 발생", exception);
        return Response.error("INTERNAL_SERVER_ERROR", "SYSTEM ERROR :: " + exception.getMessage());
    }

}