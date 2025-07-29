package backend.airo.api.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 게시물 단건 조회 요청 DTO
 */
@Schema(description = "게시물 단건 조회 요청")
public record PostDetailRequest(
        @Schema(description = "이미지 포함 여부", example = "true")
        Boolean includeImages,

        @Schema(description = "태그 포함 여부", example = "true")
        Boolean includeTags,

        @Schema(description = "댓글 포함 여부", example = "false")
        Boolean includeComments,

        @Schema(description = "조회수 증가 여부", example = "true")
        Boolean incrementViewCount
) {
    public PostDetailRequest {
        // 기본값 설정
        if (includeImages == null) includeImages = true;
        if (includeTags == null) includeTags = true;
        if (includeComments == null) includeComments = false;
        if (incrementViewCount == null) incrementViewCount = true;
    }
}