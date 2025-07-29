package backend.airo.domain.post.command;

import backend.airo.domain.post.enums.PostStatus;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 게시물 수정 커맨드
 */
public record UpdatePostCommand(
        @NotNull(message = "게시물 ID는 필수입니다")
        @Positive(message = "게시물 ID는 양수여야 합니다")
        Long postId,

        @NotNull(message = "요청자 ID는 필수입니다")
        @Positive(message = "요청자 ID는 양수여야 합니다")
        Long requesterId,

        @Size(max = 200, message = "제목은 200자를 초과할 수 없습니다")
        String title,

        @Size(max = 5000, message = "내용은 5000자를 초과할 수 없습니다")
        String content,

        PostStatus status,

        @Positive(message = "카테고리 ID는 양수여야 합니다")
        Integer categoryId,

        @Positive(message = "위치 ID는 양수여야 합니다")
        Long locationId,

        @PastOrPresent(message = "여행 날짜는 현재 또는 과거여야 합니다")
        LocalDateTime travelDate,

        @Size(max = 10, message = "이미지는 최대 10개까지 업로드 가능합니다")
        List<@NotNull Long> imageIds,

        @Size(max = 20, message = "태그는 최대 20개까지 추가 가능합니다")
        List<@NotBlank @Size(max = 50) String> tags,

        Boolean isFeatured,

        String changeReason
) {

    /**
     * 제목만 수정
     */
    public static UpdatePostCommand updateTitle(Long postId, Long requesterId, String title) {
        return new UpdatePostCommand(
                postId, requesterId, title, null, null, null, null, null,
                null, null, null, "제목 수정"
        );
    }

    /**
     * 내용만 수정
     */
    public static UpdatePostCommand updateContent(Long postId, Long requesterId, String content) {
        return new UpdatePostCommand(
                postId, requesterId, null, content, null, null, null, null,
                null, null, null, "내용 수정"
        );
    }

    /**
     * 상태만 변경
     */
    public static UpdatePostCommand changeStatus(Long postId, Long requesterId, PostStatus status, String reason) {
        return new UpdatePostCommand(
                postId, requesterId, null, null, status, null, null, null,
                null, null, null, reason
        );
    }

    /**
     * 발행으로 상태 변경
     */
    public static UpdatePostCommand publish(Long postId, Long requesterId) {
        return changeStatus(postId, requesterId, PostStatus.PUBLISHED, "게시물 발행");
    }

    /**
     * 임시저장으로 상태 변경
     */
    public static UpdatePostCommand toDraft(Long postId, Long requesterId) {
        return changeStatus(postId, requesterId, PostStatus.DRAFT, "임시저장으로 변경");
    }

    /**
     * 보관으로 상태 변경
     */
    public static UpdatePostCommand archive(Long postId, Long requesterId) {
        return changeStatus(postId, requesterId, PostStatus.ARCHIVED, "게시물 보관");
    }

    /**
     * 수정 사항이 있는지 확인
     */
    public boolean hasChanges() {
        return title != null || content != null || status != null ||
                categoryId != null || locationId != null || travelDate != null ||
                imageIds != null || tags != null || isFeatured != null;
    }

    /**
     * 상태 변경 요청인지 확인
     */
    public boolean isStatusChange() {
        return status != null;
    }

    /**
     * 메타데이터만 수정하는지 확인 (제목, 내용 외)
     */
    public boolean isMetadataOnly() {
        return title == null && content == null &&
                (categoryId != null || locationId != null || isFeatured != null);
    }
}