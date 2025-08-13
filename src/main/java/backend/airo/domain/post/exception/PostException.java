package backend.airo.domain.post.exception;

import backend.airo.common.exception.AiroException;
import backend.airo.common.exception.BaseErrorCode;
import backend.airo.domain.post.enums.PostStatus;

/**
 * 게시물 관련 비즈니스 예외의 기본 클래스
 */
public class PostException extends AiroException {

    // 기본 생성자
    public PostException(BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
    }

    // PostStatusChangeException을 대체하는 정적 팩토리 메서드
    public static PostException statusChange(Long postId, PostStatus currentStatus, PostStatus targetStatus, BaseErrorCode errorCode) {
        return new PostStatusChangePostException(errorCode, "DOMAIN", postId, currentStatus, targetStatus);
    }

    // PostValidationException을 대체하는 정적 팩토리 메서드
    public static PostException validation(BaseErrorCode errorCode, String field, String value) {
        return new PostValidationPostException(errorCode, "DOMAIN", field, value);
    }

    public static PostException validation(BaseErrorCode errorCode, String field, String value, String layer) {
        return new PostValidationPostException(errorCode, layer, field, value);
    }

    // PostPublishException을 대체하는 정적 팩토리 메서드 (기본 Post 예외로 처리)
    public static PostException publish(BaseErrorCode errorCode) {
        return new PostException(errorCode, "DOMAIN");
    }

    public static PostException publish(BaseErrorCode errorCode, String layer) {
        return new PostException(errorCode, layer);
    }

    // 내부 클래스들
    private static class PostStatusChangePostException extends PostException {
        private final Long postId;
        private final PostStatus currentStatus;
        private final PostStatus targetStatus;

        public PostStatusChangePostException(BaseErrorCode errorCode, String sourceLayer,
                                             Long postId, PostStatus currentStatus, PostStatus targetStatus) {
            super(errorCode, sourceLayer);
            this.postId = postId;
            this.currentStatus = currentStatus;
            this.targetStatus = targetStatus;
        }

        public Long getPostId() {
            return postId;
        }

        @Override
        public String getMessage() {
            return String.format("%s - 잘못된 게시물 상태 변경입니다. PostID: %d, 현재상태: %s, 변경시도상태: %s",
                    getSourceLayer(), postId, currentStatus, targetStatus);
        }
    }

    private static class PostValidationPostException extends PostException {
        private final String field;
        private final String value;

        public PostValidationPostException(BaseErrorCode errorCode, String sourceLayer,
                                           String field, String value) {
            super(errorCode, sourceLayer);
            this.field = field;
            this.value = value;
        }

        public String getField() {
            return field;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String getMessage() {
            return String.format("%s (필드: %s, 값: %s)",
                    super.getErrorCode().getErrorReason().message(), field, value);
        }
    }


    public static PostException notFound(Long postId) {
        return new NotFoundPostException(
                PostErrorCode.POST_NOT_FOUND,
                "DOMAIN",
                postId
        );
    }

    public static PostException notFound(Long postId, String layer) {
        return new NotFoundPostException(
                PostErrorCode.POST_NOT_FOUND,
                layer,
                postId
        );
    }

    // 내부 클래스 추가
    private static class NotFoundPostException extends PostException {
        private final Long postId;

        public NotFoundPostException(BaseErrorCode errorCode, String sourceLayer, Long postId) {
            super(errorCode, sourceLayer);
            this.postId = postId;
        }

        public Long getPostId() {
            return postId;
        }

        @Override
        public String getMessage() {
            String baseMessage = super.getMessage();
            return String.format("%s - 게시물 ID: %d", baseMessage, postId);
        }
    }

    public static PostException accessDenied(Long postId, Long requesterId) {
        return new AccessDeniedPostException(
                PostErrorCode.POST_ACCESS_DENIED,
                "DOMAIN",
                postId,
                requesterId
        );
    }

    public static PostException accessDenied(Long postId, Long requesterId, String layer) {
        return new AccessDeniedPostException(
                PostErrorCode.POST_ACCESS_DENIED,
                layer,
                postId,
                requesterId
        );
    }

    // 내부 클래스 추가
    private static class AccessDeniedPostException extends PostException {
        private final Long postId;
        private final Long requesterId;

        public AccessDeniedPostException(BaseErrorCode errorCode, String sourceLayer,
                                         Long postId, Long requesterId) {
            super(errorCode, sourceLayer);
            this.postId = postId;
            this.requesterId = requesterId;
        }

        public Long getPostId() {
            return postId;
        }

        public Long getRequesterId() {
            return requesterId;
        }

        @Override
        public String getMessage() {
            String baseMessage = super.getMessage();
            return String.format("%s - 게시물 ID: %d, 요청자 ID: %d", baseMessage, postId, requesterId);
        }
    }
}