package backend.airo.cache.post.dto;

import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.*;
import backend.airo.domain.post.vo.Location;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class PostCacheDto {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String summary;
    private String businessName;
    private PostStatus status;
    private PostForWhatTag forWhatTag;
    private PostCategory category;
    private LocalDate travelDate;
    private Location location;
    private String address;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Boolean isFeatured;
    private LocalDateTime publishedAt;
    private List<PostEmotionTag> emotionTags;

    public static PostCacheDto from(Post post) {
        return new PostCacheDto(
                post.getId(),
                post.getUserId(),
                post.getTitle(),
                post.getContent(),
                post.getSummary(),
                post.getBusinessName(),
                post.getStatus(),
                post.getForWhatTag(),
                post.getCategory(),
                post.getTravelDate(),
                post.getLocation(),
                post.getAddress(),
                post.getViewCount(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getIsFeatured(),
                post.getPublishedAt(),
                post.getEmotionTags() != null ?
                        new ArrayList<>(post.getEmotionTags()) : new ArrayList<>()
        );
    }

    public Post toPost() {
        return new Post(
                id, userId, title, content, summary,
                businessName,status, forWhatTag,
                emotionTags, category, travelDate, location,
                address, viewCount, likeCount, commentCount,
                isFeatured, publishedAt
        );
    }
}