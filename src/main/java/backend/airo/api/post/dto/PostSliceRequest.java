package backend.airo.api.post.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import java.util.Objects;

public record PostSliceRequest(
        @Min(value = 1, message = "사이즈는 1 이상이어야 합니다")
        @Max(value = 100, message = "사이즈는 100 이하여야 합니다")
        int size,

        @Positive(message = "마지막 게시물 ID는 양수여야 합니다")
        Long lastPostId
) {
    public PostSliceRequest {
        if (size <= 0) size = 20; // 기본값
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, lastPostId);
    }
}