package backend.airo.api.image.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "이미지 수정 요청")
public record ImageUpdateRequest(
        @Schema(description = "이미지 캡션", example = "수정된 캡션")
        String caption,

        @Schema(description = "Alt 텍스트", example = "수정된 Alt 텍스트")
        String altText,

        @Schema(description = "정렬 순서", example = "2")
        Integer sortOrder
) {}
