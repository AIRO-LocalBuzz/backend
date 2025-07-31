package backend.airo.api.post.dto;

import backend.airo.domain.post.enums.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 게시물 생성 요청 DTO
 */
@Schema(description = "게시물 생성 요청")
public record PostCreateRequest(
        @Schema(description = "게시물 제목", example = "부산 여행 후기")
        @NotBlank(message = "제목은 필수입니다")
        @Size(max = 200, message = "제목은 200자를 초과할 수 없습니다")
        String title,

        @Schema(description = "게시물 내용", example = "정말 좋은 여행이었습니다...")
        @Size(max = 5000, message = "내용은 5000자를 초과할 수 없습니다")
        String content,

        @Schema(description = "작성자 ID", example = "1")
        @NotNull(message = "작성자 ID는 필수입니다")
        @Positive(message = "작성자 ID는 양수여야 합니다")
        Long userId,

        @Schema(description = "게시물 상태", example = "DRAFT")
        PostStatus status,

        @Schema(description = "카테고리 ID", example = "1")
        @Positive(message = "카테고리 ID는 양수여야 합니다")
        Long categoryId,

        @Schema(description = "위치 ID", example = "1")
        @Positive(message = "위치 ID는 양수여야 합니다")
        Long locationId,

        @Schema(description = "여행 날짜", example = "2024-08-15T10:30:00")
        @PastOrPresent(message = "여행 날짜는 현재 또는 과거여야 합니다")
        LocalDateTime travelDate,

        @Schema(description = "이미지 ID 목록", example = "[1, 2, 3]")
        @Size(max = 10, message = "이미지는 최대 10개까지 업로드 가능합니다")
        List<@NotNull Long> imageIds,

        @Schema(description = "태그 목록", example = "[\"부산\", \"여행\", \"맛집\"]")
        @Size(max = 20, message = "태그는 최대 20개까지 추가 가능합니다")
        List<@NotBlank @Size(max = 50) String> tags,

        @Schema(description = "추천 게시물 여부", example = "false")
        Boolean isFeatured
) {

        public static PostCreateRequest forDraft(String title, String content, Long userId) {
                return new PostCreateRequest(
                        title, content, userId, PostStatus.DRAFT,
                        null, null, null, List.of(), List.of(), false
                );
        }

        /**
         * 발행 커맨드 생성
         */
        public static PostCreateRequest forPublish(String title, String content, Long userId,
                                                   Long categoryId, Long locationId) {
                return new PostCreateRequest(
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