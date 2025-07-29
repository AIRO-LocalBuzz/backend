package backend.airo.domain.image;

import backend.airo.domain.post.Post;
import backend.airo.persistence.post.entity.PostEntity;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@RequiredArgsConstructor
@Getter
@Setter
public class Image {
    private final Long id;
    private final Long userId;
    private String originalFilename;
    private String storedFilename;
    private String imageUrl;
    private String altText;
    private String caption;
    private Long fileSize;
    private String mimeType;
    private Integer width;
    private Integer height;
    private Integer sortOrder;
    private Boolean isCover;
    private LocalDateTime createdAt;
    private Post post;


    public Image(Long id, Long userId, String originalFilename, String storedFilename, String imageUrl, String altText, String caption, Long fileSize, String mimeType, Integer width, Integer height, Integer sortOrder, Boolean isCover, LocalDateTime createdAt, Post post) {
        this.id = id;
        this.userId = userId;
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.imageUrl = imageUrl;
        this.altText = altText;
        this.caption = caption;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.width = width;
        this.height = height;
        this.sortOrder = sortOrder;
        this.isCover = isCover;
        this.createdAt = createdAt;
        this.post = post;
    }



}
