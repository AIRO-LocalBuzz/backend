package backend.airo.api.image.dto;

import backend.airo.domain.image.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "이미지 응답")
public record ImageResponse(
        @Schema(description = "이미지 ID", example = "1")
        Long id,

        @Schema(description = "게시물 ID", example = "1")
        Long postId,

        @Schema(description = "사용자 ID", example = "1")
        Long userId,

        @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
        String imageUrl,


        @Schema(description = "MIME 타입", example = "image/jpeg")
        String mimeType,

        @Schema(description = "파일 크기 (bytes)", example = "1048576")
        Long fileSize,

        @Schema(description = "이미지 너비", example = "1920")
        Integer width,

        @Schema(description = "이미지 높이", example = "1080")
        Integer height,

        @Schema(description = "이미지 캡션", example = "아름다운 바다 풍경")
        String caption,

        @Schema(description = "Alt 텍스트", example = "바다와 하늘이 보이는 해변 풍경")
        String altText,

        @Schema(description = "정렬 순서", example = "1")
        Integer sortOrder,

        @Schema(description = "생성일시", example = "2024-07-29T12:34:56")
        LocalDateTime createdAt
) {
    public static ImageResponse from(Image image) {
        return ImageResponse.builder()
                .id(image.getId())
                .postId(image.getPostId())
                .userId(image.getUserId())
                .imageUrl(image.getImageUrl())
                .mimeType(image.getMimeType())
                .fileSize(image.getFileSize())
                .width(image.getWidth())
                .height(image.getHeight())
                .caption(image.getCaption())
                .altText(image.getAltText())
                .sortOrder(image.getSortOrder())
                .createdAt(image.getCreatedAt())
                .build();
    }
}