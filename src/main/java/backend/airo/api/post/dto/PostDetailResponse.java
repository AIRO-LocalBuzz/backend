package backend.airo.api.post.dto;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 게시물 상세 응답 DTO
 */
@Schema(description = "게시물 상세 응답")
public record PostDetailResponse(
        @Schema(description = "게시물 ID", example = "1")
        Long id,

        @Schema(description = "게시물 제목", example = "부산 여행 후기")
        String title,

        @Schema(description = "게시물 내용", example = "정말 좋은 여행이었습니다...")
        String content,

        @Schema(description = "게시물 요약", example = "부산에서의 즐거운 여행...")
        String summary,

        @Schema(description = "게시물 상태", example = "PUBLISHED")
        PostStatus status,

        @Schema(description = "여행 날짜", example = "2024-08-15T10:30:00")
        LocalDateTime travelDate,

        @Schema(description = "조회수", example = "150")
        Integer viewCount,

        @Schema(description = "좋아요 수", example = "25")
        Integer likeCount,

        @Schema(description = "댓글 수", example = "8")
        Integer commentCount,

        @Schema(description = "추천 게시물 여부", example = "false")
        Boolean isFeatured,

        @Schema(description = "생성 시간", example = "2024-08-10T09:00:00")
        LocalDateTime createdAt,

        @Schema(description = "발행 시간", example = "2024-08-10T10:00:00")
        LocalDateTime publishedAt,

        @Schema(description = "작성자 정보")
        AuthorInfo author,

        @Schema(description = "이미지 목록")
        List<ImageInfo> images,

        @Schema(description = "태그 목록")
        List<TagInfo> tags,

        @Schema(description = "댓글 목록")
        List<CommentInfo> comments
) {
    public static PostDetailResponse fromDomain(Post post) {
        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getSummary(),
                post.getStatus(),
                post.getTravelDate(),
                post.getViewCount(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getIsFeatured(),
                post.getCreatedAt(),
                post.getPublishedAt(),
                // TODO: 실제 구현에서 연관 데이터 매핑
                null, // author
                List.of(), // images
                List.of(), // tags
                List.of()  // comments
        );
    }

    @Schema(description = "작성자 정보")
    public record AuthorInfo(
            Long id,
            String username,
            String profileImageUrl
    ) {}

    @Schema(description = "이미지 정보")
    public record ImageInfo(
            Long id,
            String url,
            String alt,
            Integer sortOrder
    ) {}

    @Schema(description = "태그 정보")
    public record TagInfo(
            Integer id,
            String name
    ) {}

    @Schema(description = "댓글 정보")
    public record CommentInfo(
            Long id,
            String content,
            String authorName,
            LocalDateTime createdAt
    ) {}
}