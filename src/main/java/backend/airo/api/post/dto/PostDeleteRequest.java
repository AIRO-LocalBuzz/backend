package backend.airo.api.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * 게시물 삭제 요청 DTO
 */
@Schema(description = "게시물 삭제 요청")
public record PostDeleteRequest(
        @Schema(description = "삭제 사유", example = "중복 게시물")
        @Size(max = 200, message = "삭제 사유는 200자를 초과할 수 없습니다")
        String deleteReason,

        @Schema(description = "강제 삭제 여부", example = "false")
        Boolean forceDelete
) {
    public PostDeleteRequest {
        // 기본값 설정
        if (forceDelete == null) forceDelete = false;
    }
}