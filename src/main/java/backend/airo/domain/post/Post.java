package backend.airo.domain.post;

import backend.airo.domain.post.enums.PostStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class Post {
    private final Long id;
    private String title;
    private String content;
    private String authorEmail;
    private String summary;
    private PostStatus status;
    private Integer likeCount = 0;
    private Integer commentCount = 0;
    private Integer viewCount = 0;
    private Boolean isFeatured = false;
    private LocalDateTime travelDate;
    private LocalDateTime publishedAt;
    private String authorName;
    private String authorNickname;
    private String authorProfileImageUrl;



    public Post(Long id, String title, String content, String summary, PostStatus status, LocalDateTime travelDate, Integer viewCount, Integer likeCount, Integer commentCount ,Boolean isFeatured, LocalDateTime publishedAt ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.status = status;
        this.travelDate = travelDate;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.isFeatured = isFeatured;
        this.publishedAt = publishedAt;
    }

}

