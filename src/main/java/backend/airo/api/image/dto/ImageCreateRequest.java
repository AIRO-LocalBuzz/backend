package backend.airo.api.image.dto;

import backend.airo.domain.image.Image;
import io.swagger.v3.oas.annotations.media.Schema;

public record ImageCreateRequest(
    @Schema(description = "게시물 ID", example = "1")
    Long postId,

    @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
    String imageUrl,

    @Schema(description = "MIME 타입", example = "image/jpeg")
    String mimeType,

    @Schema(description = "파일 크기 (bytes)", example = "1048576")
    Long fileSize,

    @Schema(description = "이미지 캡션", example = "아름다운 바다 풍경")
    String caption,

    @Schema(description = "Alt 텍스트", example = "바다와 하늘이 보이는 해변 풍경")
    String altText,

    @Schema(description = "정렬 순서", example = "1")
    Integer sortOrder

    ) {
    public Image toImage(Long userId) {
        return Image.builder()
                .postId(postId)
                .userId(userId)
                .imageUrl(imageUrl)
                .mimeType(mimeType)
                .fileSize(fileSize)
                .caption(caption)
                .altText(altText)
                .sortOrder(sortOrder)
                .build();
    }
}
