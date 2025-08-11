package backend.airo.api.global.config;

import backend.airo.common.exception.BaseErrorCode;
import backend.airo.domain.post.exception.PostErrorCode;
import backend.airo.domain.image.exception.ImageErrorCode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ValidationErrorMappingConfig {

    private final Map<String, BaseErrorCode> pathVariableErrorMap = new HashMap<>();
    private final Map<String, BaseErrorCode> requestBodyFieldErrorMap = new HashMap<>();

    public ValidationErrorMappingConfig() {
        initializePathVariableErrors();
        initializeRequestBodyFieldErrors();
    }

    private void initializePathVariableErrors() {
        // Post 도메인
        pathVariableErrorMap.put("postId", PostErrorCode.POST_ID_REQUIRED);

        // User 도메인
//        pathVariableErrorMap.put("userId", UserErrorCode.USER_ID_REQUIRED);

        // Image 도메인
        pathVariableErrorMap.put("imageId", ImageErrorCode.IMAGE_ID_REQUIRED);

        // Category 도메인
        pathVariableErrorMap.put("categoryId", PostErrorCode.POST_PUBLISH_CATEGORY_REQUIRED);

        // Location 도메인
        pathVariableErrorMap.put("locationId", PostErrorCode.POST_PUBLISH_LOCATION_REQUIRED);
    }

    private void initializeRequestBodyFieldErrors() {
        // Post 도메인
        requestBodyFieldErrorMap.put("title", PostErrorCode.POST_TITLE_REQUIRED);
        requestBodyFieldErrorMap.put("content", PostErrorCode.POST_CONTENT_REQUIRED);
        requestBodyFieldErrorMap.put("status", PostErrorCode.POST_STATUS_REQUIRED);

        // User 도메인
//        requestBodyFieldErrorMap.put("email", UserErrorCode.USER_EMAIL_REQUIRED);
//        requestBodyFieldErrorMap.put("password", UserErrorCode.USER_PASSWORD_REQUIRED);
//        requestBodyFieldErrorMap.put("nickname", UserErrorCode.USER_NICKNAME_REQUIRED);

        // 공통
        requestBodyFieldErrorMap.put("name", PostErrorCode.POST_TITLE_REQUIRED); // 기본값
    }

    public BaseErrorCode getPathVariableError(String parameterName) {
        return pathVariableErrorMap.getOrDefault(parameterName, getDefaultPathVariableError());
    }

    public BaseErrorCode getRequestBodyFieldError(String fieldName) {
        return requestBodyFieldErrorMap.getOrDefault(fieldName, getDefaultRequestBodyError());
    }

    private BaseErrorCode getDefaultPathVariableError() {
        return PostErrorCode.POST_REQUEST_REQUIRED; // 기본 에러
    }

    private BaseErrorCode getDefaultRequestBodyError() {
        return PostErrorCode.POST_REQUEST_REQUIRED; // 기본 에러
    }
}