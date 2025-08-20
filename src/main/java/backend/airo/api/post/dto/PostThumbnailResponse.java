package backend.airo.api.post.dto;

import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.domain.thumbnail.ThumbnailResult;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "게시물 응답")
public record PostThumbnailResponse(
        // 기존 필드들
        Long id,
        String title,
        String summary,
        PostStatus status,
        LocalDate travelDate,
        Integer viewCount,
        Integer likeCount,
        Integer commentCount,
        Boolean isFeatured,
        LocalDateTime createdAt,

        // 썸네일 정보를 별도 객체로
        @Schema(description = "AI 생성 썸네일 정보")
        ThumbnailInfo thumbnail
) {

    @Schema(description = "썸네일 정보")
    public record ThumbnailInfo(
            String spotName,
            String mainImageUrl,
            List<String> recommendedTags,
            List<String> emotions,
            String suggestedTitle
    ) {}

    public static PostThumbnailResponse fromDomain(Post post, ThumbnailResult thumbnail) {
        ThumbnailInfo thumbnailInfo = thumbnail != null ?
                new ThumbnailInfo(
                        thumbnail.spotName(),
                        thumbnail.mainImageUrl(),
                        thumbnail.recommendedTags(),
                        thumbnail.emotions(),
                        thumbnail.suggestedTitle()
                ) : null;

        return new PostThumbnailResponse(
                post.id(),
                post.title(),
                post.summary(),
                post.status(),
                post.travelDate(),
                post.viewCount(),
                post.likeCount(),
                post.commentCount(),
                post.isFeatured(),
                post.publishedAt(),
                thumbnailInfo
        );
    }
}