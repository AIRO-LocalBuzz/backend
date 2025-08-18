package backend.airo.api.post.dto;

import backend.airo.domain.post.enums.*;
import backend.airo.domain.post.vo.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "게시물 수정 요청")
public record PostUpdateRequest(
        @Schema(description = "게시물 제목", example = "수정된 부산 여행 후기")
        @Size(max = 200, message = "제목은 200자를 초과할 수 없습니다")
        String title,

        @Schema(description = "게시물 내용", example = "수정된 내용입니다...")
        @Size(max = 5000, message = "내용은 5000자를 초과할 수 없습니다")
        String content,

        @Schema(description = "게시물 상태", example = "PUBLISHED")
        PostStatus status,

        @Schema(description = "누구와 태그", example = "FRIEND")
        PostWithWhoTag withWhoTag,

        @Schema(description = "목적 태그", example = "HEALING")
        PostForWhatTag forWhatTag,

        @Schema(description = "감정 태그", example = "[\"EXCITED\", \"JOYFUL\"]")
        @Size(max = 5, message = "감정 태그는 최대 5개까지 추가 가능합니다")
        List<PostEmotionTag> emotionTags,

        @Schema(description = "카테고리", example = "RESTAURANT")
        PostCategory category,

        @Schema(description = "여행 날짜", example = "2024-08-15T10:30:00")
        @PastOrPresent(message = "여행 날짜는 현재 또는 과거여야 합니다")
        LocalDate travelDate,

        @Schema(description = "위치 정보")
        Location location,

        @Schema(description = "주소", example = "부산시 해운대구")
        String address,

        @Schema(description = "추천 게시물 여부", example = "false")
        Boolean isFeatured

) {

        public boolean hasChanges() {
                return title != null || content != null || status != null ||
                        withWhoTag != null || forWhatTag != null || emotionTags != null ||
                        travelDate != null || location != null || address != null || isFeatured != null;
        }

        public boolean isStatusChange() {
                return status != null;
        }

        public boolean isMetadataOnly() {
                return title == null && content == null &&
                        (withWhoTag != null || forWhatTag != null || emotionTags != null ||
                                location != null || address != null || isFeatured != null);
        }
}