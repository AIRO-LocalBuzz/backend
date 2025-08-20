package backend.airo.domain.post;

import backend.airo.api.post.dto.PostCreateRequest;
import backend.airo.api.post.dto.PostUpdateRequest;
import backend.airo.domain.post.enums.*;
import backend.airo.domain.post.vo.Location;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class Post {
    private final Long id;
    private Long userId;
    private String title;
    private String content;
    private String summary;
    private String businessName;
    private PostStatus status;
    private PostForWhatTag forWhatTag;
    private List<PostEmotionTag> emotionTags;
    private PostCategory category;
    private LocalDate travelDate;
    private Location location;
    private String address;
    private Integer viewCount = 0;
    private Integer likeCount = 0;
    private Integer commentCount = 0;
    private Boolean isFeatured = false;
    private LocalDateTime publishedAt;

    public Post(Long id, Long userId, String title, String content, String summary, String businessName,
                PostStatus status, PostForWhatTag forWhatTag,
                List<PostEmotionTag> emotionTags, PostCategory category, LocalDate travelDate, Location location,
                String address, Integer viewCount, Integer likeCount, Integer commentCount,
                Boolean isFeatured, LocalDateTime publishedAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.businessName = businessName;
        this.status = status;
        this.forWhatTag = forWhatTag;
        this.emotionTags = emotionTags;
        this.category = category;
        this.travelDate = travelDate;
        this.location = location;
        this.address = address;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.isFeatured = isFeatured;
        this.publishedAt = publishedAt;
    }


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
                existingPost.getId(),
                existingPost.getUserId(),
                request.title() != null ? request.title() : existingPost.getTitle(),
                request.content() != null ? request.content() : existingPost.getContent(),
                existingPost.getSummary(),
                existingPost.getBusinessName(),
                request.status() != null ? request.status() : existingPost.getStatus(),
                request.forWhatTag() != null ? request.forWhatTag() : existingPost.getForWhatTag(),
                request.emotionTags() != null ? request.emotionTags() : existingPost.getEmotionTags(),
                existingPost.getCategory(),
                request.travelDate() != null ? request.travelDate() : existingPost.getTravelDate(),
                request.location() != null ? request.location() : existingPost.getLocation(),
                request.address() != null ? request.address() : existingPost.getAddress(),
                existingPost.getViewCount(),
                existingPost.getLikeCount(),
                existingPost.getCommentCount(),
                request.isFeatured() != null ? request.isFeatured() : existingPost.getIsFeatured(),
                request.status() == PostStatus.PUBLISHED && existingPost.getPublishedAt() == null
                        ? LocalDateTime.now() : existingPost.getPublishedAt()
        );
    }


    public void incrementViewCount() {
        this.viewCount++;
    }

}