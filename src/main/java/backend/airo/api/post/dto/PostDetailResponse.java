package backend.airo.api.post.dto;
import backend.airo.domain.image.Image;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.*;
import backend.airo.domain.post.vo.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import backend.airo.domain.post.vo.AuthorInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "게시물 상세 응답")
public record PostDetailResponse(
        @NotNull(message = "게시물 ID는 필수입니다")
        @Positive(message = "게시물 ID는 양수여야 합니다")
        @Schema(description = "게시물 ID", example = "1")
        Long id,

        @Schema(description = "게시물 제목", example = "부산 여행 후기")
        String title,

        @Schema(description = "게시물 내용", example = "정말 좋은 여행이었습니다...")
        String content,

        @Schema(description = "게시물 요약", example = "부산에서의 즐거운 여행...")
        String summary,

        @Schema(description = "사업장 이름", example = "짱구 분식")
        String businessName,

        @Schema(description = "게시물 상태", example = "PUBLISHED")
        PostStatus status,

        @Schema(description = "목적 태그", example = "HEALING")
        PostForWhatTag forWhatTag,

        @Schema(description = "감정 태그")
        List<PostEmotionTag> emotionTags,

        @Schema(description = "여행 날짜", example = "2024-08-15T10:30:00")
        LocalDate travelDate,

        @Schema(description = "위치 정보")
        Location location,

        @Schema(description = "주소", example = "부산시 해운대구")
        String address,

        @Schema(description = "조회수", example = "150")
        Integer viewCount,

        @Schema(description = "좋아요 수", example = "25")
        Integer likeCount,

        @Schema(description = "댓글 수", example = "8")
        Integer commentCount,

        @Schema(description = "추천 게시물 여부", example = "false")
        Boolean isFeatured,

        @Schema(description = "발행 시간", example = "2024-08-10T10:00:00")
        LocalDateTime publishedAt,

        @Schema(description = "작성자 정보")
        AuthorInfo author,

        @Schema(description = "이미지 목록")
        List<Image> images
) {
        public static PostDetailResponse toResponse(Post post,
                                                    AuthorInfo author,
                                                    List<Image> imageList
        ) {
                return new PostDetailResponse(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getSummary(),
                        post.getBusinessName(),
                        post.getStatus(),
                        post.getForWhatTag(),
                        post.getEmotionTags(),
                        post.getTravelDate(),
                        post.getLocation(),
                        post.getAddress(),
                        post.getViewCount(),
                        post.getLikeCount(),
                        post.getCommentCount(),
                        post.getIsFeatured(),
                        post.getPublishedAt(),
                        author,
                        imageList
                );
        }
}