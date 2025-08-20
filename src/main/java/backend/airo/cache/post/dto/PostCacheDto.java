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
    private PostStatus status;
    private PostWithWhoTag withWhoTag;
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
                post.id(),
                post.userId(),
                post.title(),
                post.content(),
                post.summary(),
                post.status(),
                post.withWhoTag(),
                post.forWhatTag(),
                post.category(),
                post.travelDate(),
                post.location(),
                post.address(),
                post.viewCount(),
                post.likeCount(),
                post.commentCount(),
                post.isFeatured(),
                post.publishedAt(),
                post.emotionTags() != null ?
                        new ArrayList<>(post.emotionTags()) : new ArrayList<>()
        );
    }

    public Post toPost() {
        return new Post(
                id, userId, title, content, summary,
                status, withWhoTag, forWhatTag,
                emotionTags, category, travelDate, location,
                address, viewCount, likeCount, commentCount,
                isFeatured, publishedAt
        );
    }
}