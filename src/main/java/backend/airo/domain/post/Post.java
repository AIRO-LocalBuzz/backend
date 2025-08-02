package backend.airo.domain.post;

import backend.airo.domain.post.enums.*;
import backend.airo.domain.location.Location;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Post {
    private final Long id;
    private Long userId;
    private String title;
    private String content;
    private String summary;
    private PostStatus status;
    private PostWithWhoTag withWhoTag;
    private PostForWhatTag forWhatTag;
    private List<PostEmotionTag> emotionTags;
    private PostCategory category;
    private LocalDate travelDate;
    private Location location;
    private String adress;
    private Integer viewCount = 0;
    private Integer likeCount = 0;
    private Integer commentCount = 0;
    private Boolean isFeatured = false;
    private LocalDateTime publishedAt;

    public Post(Long id, Long userId, String title, String content, String summary,
                PostStatus status, PostWithWhoTag withWhoTag, PostForWhatTag forWhatTag,
                List<PostEmotionTag> emotionTags, PostCategory category, LocalDate travelDate, Location location,
                String adress, Integer viewCount, Integer likeCount, Integer commentCount,
                Boolean isFeatured, LocalDateTime publishedAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.status = status;
        this.withWhoTag = withWhoTag;
        this.forWhatTag = forWhatTag;
        this.emotionTags = emotionTags;
        this.category = category;
        this.travelDate = travelDate;
        this.location = location;
        this.adress = adress;
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
                PostStatus.PUBLISHED,
                PostWithWhoTag.ALLONE,
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

    public void incrementViewCount() {
        this.viewCount++;
    }

}