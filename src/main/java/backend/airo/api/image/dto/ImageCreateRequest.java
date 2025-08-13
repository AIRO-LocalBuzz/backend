package backend.airo.api.image.dto;
import jakarta.validation.constraints.*;
import backend.airo.domain.image.Image;
import io.swagger.v3.oas.annotations.media.Schema;

public record ImageCreateRequest(

    @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
    @NotBlank(message = "이미지 URL은 필수입니다")
    String imageUrl,

    @Schema(description = "MIME 타입", example = "image/jpeg")
    @NotBlank(message = "MIME 타입은 필수입니다")
    String mimeType

    ) {
    public Image toImage(Long userId) {
        return new Image(userId, 1L, imageUrl, mimeType);
    }
}
