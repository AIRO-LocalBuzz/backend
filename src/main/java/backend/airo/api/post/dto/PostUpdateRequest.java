package backend.airo.api.post.dto;

import backend.airo.domain.post.enums.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 게시물 수정 요청 DTO
 */
@Schema(description = "게시물 수정 요청")
public record PostUpdateRequest(
        @Schema(description = "게시물 제목", example = "수정된 부산 여행 후기")
        @Size(max = 200, message = "제목은 200자를 초과할 수 없습니다")
        String title,

        @Schema(description = "게시물 내용", example = "수정된 내용입니다...")
        @Size(max = 5000, message = "내용은 5000자를 초과할 수 없습니다")
        String content,

        @Schema(description = "게시물 상태", example = "PUBLISHED")
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
        Boolean isFeatured,

        @Schema(description = "변경 사유", example = "내용 보완")
        String changeReason
) {
}
