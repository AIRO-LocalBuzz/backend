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
        List<String> tags
) {
    public PostListRequest {
        if (page == null) page = 0;
        if (size == null) size = 20;
    }
}