package backend.airo.domain.post;

import backend.airo.domain.post.enums.PostStatus;
import com.nimbusds.openid.connect.sdk.claims.PersonClaims;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class Post {
    private final Long id;
    private final Long userId;
    private final Long categoryId;
    private final Long locationId;
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
    private LocalDateTime createdAt = LocalDateTime.now();
    private String authorName;
    private String authorNickname;
    private String authorProfileImageUrl;


    public Post(Long id, Long userId, Long categoryId, Long locationId, String title, String content, String summary, PostStatus status, LocalDateTime travelDate, Integer viewCount, Integer likeCount, Integer commentCount ,Boolean isFeatured, LocalDateTime publishedAt ) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.locationId = locationId;
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

    public void incrementViewCount() {
        this.viewCount++;
    }


}

