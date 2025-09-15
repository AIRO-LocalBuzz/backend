package backend.airo.api.post.dto;

import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 게시물 응답 DTO
 */
@Schema(description = "게시물 응답")
public record PostResponse(
        @Schema(description = "게시물 ID", example = "1")
        Long id,

        @Schema(description = "게시물 제목", example = "부산 여행 후기")
        String title,

        @Schema(description = "게시물 요약", example = "부산에서의 즐거운 여행...")
        String summary,

        @Schema(description = "게시물 상태", example = "PUBLISHED")
        PostStatus status,

        @Schema(description = "여행 날짜", example = "2024-08-15T10:30:00")
        LocalDate travelDate,

        @Schema(description = "조회수", example = "150")
        Integer viewCount,

        @Schema(description = "좋아요 수", example = "25")
        Integer likeCount,

        @Schema(description = "댓글 수", example = "8")
        Integer commentCount,

        @Schema(description = "추천 게시물 여부", example = "false")
        Boolean isFeatured,

        @Schema(description = "생성 시간", example = "2024-08-10T09:00:00")
        LocalDateTime createdAt


) {
    public static PostResponse fromDomain(Post post) {
        return new PostResponse(
                post.id(),
                post.title(),
                post.summary(),
                post.status(),
                post.travelDate(),
                post.viewCount(),
                post.likeCount(),
                post.commentCount(),
                post.isFeatured(),
                post.publishedAt()
        );
    }
}