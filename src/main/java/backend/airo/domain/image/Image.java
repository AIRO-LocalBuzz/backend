package backend.airo.domain.image;

import backend.airo.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@RequiredArgsConstructor
@Getter
@Setter
public class Image {
    private final Long id;
    private Long userId;
    private Long postId;
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


    public Image(Long id, Long userId, Long postId, String originalFilename, String storedFilename, String imageUrl, String altText, String caption, Long fileSize, String mimeType, Integer width, Integer height, Integer sortOrder, Boolean isCover) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
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
    }

    public Image (Long userId, Long postId, String imageUrl, String mimeType, Integer sortOrder) {
        this.id = null;
        this.postId = postId;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.mimeType = mimeType;
        this.sortOrder = sortOrder;
        this.isCover = false; // 기본값 설정
    }

    public Image (Long userId, Long postId, String imageUrl, String mimeType) {
        this.id = null;
        this.userId = userId;
        this.postId = postId; // 게시물 ID가 없는 경우
        this.imageUrl = imageUrl;
        this.mimeType = mimeType;
        this.sortOrder = 0;
        this.isCover = false; // 기본값 설정
    }


}
