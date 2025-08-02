package backend.airo.api.image.dto;

import backend.airo.domain.image.Image;
import io.swagger.v3.oas.annotations.media.Schema;

public record ImageCreateRequest(

    @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
    String imageUrl,

    @Schema(description = "MIME 타입", example = "image/jpeg")
    String mimeType

    ) {
    public Image toImage(Long userId) {
        return new Image(userId, imageUrl, mimeType);
    }
}
