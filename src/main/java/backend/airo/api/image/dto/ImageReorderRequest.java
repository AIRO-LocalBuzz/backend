package backend.airo.api.image.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "이미지 순서 재정렬 요청")
public record ImageReorderRequest(
        @Schema(description = "재정렬할 이미지 ID 목록 (원하는 순서대로)",
                example = "[3, 1, 2]")
        List<Long> imageIds
) {}