package backend.airo.api.post.dto;

import backend.airo.api.image.dto.ImageCreateRequest;
import backend.airo.domain.post.enums.*;
import backend.airo.domain.location.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "게시물 생성 요청")
public record PostCreateRequest(
        @Schema(description = "게시물 제목", example = "부산 여행 후기")
        @NotBlank(message = "제목은 필수입니다")
        @Size(max = 200, message = "제목은 200자를 초과할 수 없습니다")
        String title,

        @Schema(description = "게시물 내용", example = "정말 좋은 여행이었습니다...")
        @Size(max = 5000, message = "내용은 5000자를 초과할 수 없습니다")
        String content,

        @Schema(description = "게시물 상태", example = "DRAFT")
        PostStatus status,

        @Schema(description = "누구와 태그", example = "FRIEND")
        PostWithWhoTag withWhoTag,

        @Schema(description = "목적 태그", example = "HEALING")
        PostForWhatTag forWhatTag,

        @Schema(description = "감정 태그", example = "[\"EXCITED\", \"JOYFUL\"]")
        @Size(max = 5, message = "감정 태그는 최대 5개까지 추가 가능합니다")
        List<PostEmotionTag> emotionTags,

        @Schema(description = "카테고리", example = "RESTORANT")
        PostCategory category,

        @Schema(description = "여행 날짜", example = "2024-08-15T10:30:00")
        @PastOrPresent(message = "여행 날짜는 현재 또는 과거여야 합니다")
        LocalDate travelDate,
        //Todo: String이나 시간 제외

//        @Schema(description = "위치 정보")
//        Location location,

        @Schema(description = "주소", example = "부산시 해운대구")
        String adress,

        @Schema(description = "이미지 생성 요청 목록")
        @Size(max = 5, message = "이미지는 최대 5개까지 업로드 가능합니다")
        List<ImageCreateRequest> images,

        @Schema(description = "추천 게시물 여부", example = "false")
        Boolean isFeatured
) {

        public static PostCreateRequest forDraft(String title, String content) {
                return new PostCreateRequest(
                        title, content, PostStatus.DRAFT,
                        null, null, null, null, null, null,
                        List.of(), false
                );
        }

        public static PostCreateRequest forPublish(String title, String content,
                                                   PostWithWhoTag withWhoTag, PostForWhatTag forWhatTag,
                                                   PostCategory category) {
                return new PostCreateRequest(
                        title, content, PostStatus.PUBLISHED,
                        withWhoTag, forWhatTag, null, category, null, null,
                        List.of(), false
                );
        }

        public boolean canPublish() {
                return status == PostStatus.PUBLISHED
                        && title != null && !title.trim().isEmpty()
                        && content != null && !content.trim().isEmpty()
                        && withWhoTag != null
                        && forWhatTag != null
                        && category != null;
        }

        public boolean hasImages() {
                return images != null && !images.isEmpty();
        }

        public boolean hasEmotionTags() {
                return emotionTags != null && !emotionTags.isEmpty();
        }


}