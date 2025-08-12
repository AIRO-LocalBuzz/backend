package backend.airo.api.post.dto;

import backend.airo.domain.post.enums.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;

/**
 * 게시물 목록 조회 요청 DTO
 */
@Schema(description = "게시물 목록 조회 요청")
public record PostListRequest(
        @Min(value = 0) Integer page,
        @Min(value = 1) @Max(value = 100) Integer size,
        String keyword,
        List<String> tags,
        @Schema(description = "정렬 기준 (예: likeCount, viewCount, publishedAt)", defaultValue = "publishedAt")
        String sortBy,
        @Schema(description = "게시물 상태 (예: PUBLISHED, DRAFT)", defaultValue = "PUBLISHED")
        PostStatus status
) {
    public PostListRequest {
        if (page == null) page = 0;
        if (size == null) size = 20;
        if (sortBy == null) sortBy = "publishedAt";
        if (status == null) status = PostStatus.PUBLISHED;
    }
}