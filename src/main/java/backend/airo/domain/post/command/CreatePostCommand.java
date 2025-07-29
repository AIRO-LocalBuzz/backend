package backend.airo.domain.post.command;

import backend.airo.domain.post.enums.PostStatus;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 게시물 생성 커맨드
 */
public record CreatePostCommand(
        @NotBlank(message = "제목은 필수입니다")
        @Size(max = 200, message = "제목은 200자를 초과할 수 없습니다")
        String title,

        @Size(max = 5000, message = "내용은 5000자를 초과할 수 없습니다")
        String content,

        @NotNull(message = "작성자 ID는 필수입니다")
        @Positive(message = "작성자 ID는 양수여야 합니다")
        Long userId,

        @NotNull(message = "게시물 상태는 필수입니다")
        PostStatus status,

        @Positive(message = "카테고리 ID는 양수여야 합니다")
        Long categoryId,

        @Positive(message = "위치 ID는 양수여야 합니다")
        Long locationId,

        @PastOrPresent(message = "여행 날짜는 현재 또는 과거여야 합니다")
        LocalDateTime travelDate,

        @Size(max = 10, message = "이미지는 최대 10개까지 업로드 가능합니다")
        List<@NotNull Long> imageIds,

        @Size(max = 20, message = "태그는 최대 20개까지 추가 가능합니다")
        List<@NotBlank @Size(max = 50) String> tags,

        Boolean isFeatured
) {

    public CreatePostCommand {
        // 기본값 설정
        if (status == null) {
            status = PostStatus.DRAFT;
        }
        if (imageIds == null) {
            imageIds = List.of();
        }
        if (tags == null) {
            tags = List.of();
        }
        if (isFeatured == null) {
            isFeatured = false;
        }
    }

    /**
     * 임시저장 커맨드 생성
     */
    public static CreatePostCommand forDraft(String title, String content, Long userId) {
        return new CreatePostCommand(
                title, content, userId, PostStatus.DRAFT,
                null, null, null, List.of(), List.of(), false
        );
    }

    /**
     * 발행 커맨드 생성
     */
    public static CreatePostCommand forPublish(String title, String content, Long userId,
                                               Long categoryId, Long locationId) {
        return new CreatePostCommand(
                title, content, userId, PostStatus.PUBLISHED,
                categoryId, locationId, null, List.of(), List.of(), false
        );
    }

    /**
     * 발행 가능 여부 검증
     */
    public boolean canPublish() {
        return status == PostStatus.PUBLISHED
                && title != null && !title.trim().isEmpty()
                && content != null && !content.trim().isEmpty()
                && categoryId != null
                && locationId != null;
    }

    /**
     * 이미지 첨부 여부
     */
    public boolean hasImages() {
        return imageIds != null && !imageIds.isEmpty();
    }

    /**
     * 태그 첨부 여부
     */
    public boolean hasTags() {
        return tags != null && !tags.isEmpty();
    }
}