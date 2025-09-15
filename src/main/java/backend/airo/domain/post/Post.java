package backend.airo.domain.post;

import backend.airo.api.post.dto.PostCreateRequest;
import backend.airo.api.post.dto.PostUpdateRequest;
import backend.airo.domain.post.enums.*;
import backend.airo.domain.post.vo.Location;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Builder
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public record Post(
        Long id,
        Long userId,
        String title,
        String content,
        String summary,
        String businessName,
        PostStatus status,
        PostForWhatTag forWhatTag,
        List<PostEmotionTag> emotionTags,
        PostCategory category,
        LocalDate travelDate,
        Location location,
        String address,
        Integer viewCount,
        Integer likeCount,
        Integer commentCount,
        Boolean isFeatured,
        LocalDateTime publishedAt) {


    public static Post createForTest(
            Long id,
            Long userId,
            String title,
            String content,
            List<PostEmotionTag> emotionTags,
            PostCategory category,
            LocalDateTime publishedAt
    ) {
        return new Post(
                id,
                userId,
                title,
                content,
                null, // summary
                "gootshp",
                PostStatus.PUBLISHED,
                PostForWhatTag.HEALING,
                emotionTags,
                category,
                LocalDate.now(),
                new Location(0.0, 0.0), // Dummy location
                "Test Address",
                0, // viewCount
                0, // likeCount
                0, // commentCount
                false, // isFeatured
                publishedAt
        );
    }

    public static Post createPost(PostCreateRequest request, Long userId) {
        return new Post(
                null,
                userId,
                request.title(),
                request.content(),
                null, // AI로 생성될 요약
                request.businessName(),
                request.status(),
                request.forWhatTag(),
                request.emotionTags(),
                request.category(),
                request.travelDate(),
                request.location(),
                request.address(),
                0, // 초기 조회수
                0, // 초기 좋아요 수
                0, // 초기 댓글 수
                request.isFeatured(),
                LocalDateTime.now()
        );
    }

    public static Post updatePostFromCommand(Post existingPost, PostUpdateRequest request) {
        return new Post(
                existingPost.id,
                existingPost.userId,
                request.title() != null ? request.title() : existingPost.title(),
                request.content() != null ? request.content() : existingPost.content(),
                existingPost.summary,
                existingPost.businessName,
                request.status() != null ? request.status() : existingPost.status(),
                request.forWhatTag() != null ? request.forWhatTag() : existingPost.forWhatTag(),
                request.emotionTags() != null ? request.emotionTags() : existingPost.emotionTags(),
                existingPost.category,
                request.travelDate() != null ? request.travelDate() : existingPost.travelDate(),
                request.location() != null ? request.location() : existingPost.location(),
                request.address() != null ? request.address() : existingPost.address(),
                existingPost.viewCount,
                existingPost.likeCount,
                existingPost.commentCount,
                request.isFeatured() != null ? request.isFeatured() : existingPost.isFeatured(),
                request.status() == PostStatus.PUBLISHED && existingPost.publishedAt() == null
                        ? LocalDateTime.now() : existingPost.publishedAt()
        );
    }

    public boolean isPostOwner(Long userId) {
        return Objects.equals(this.userId, userId);
    }

}