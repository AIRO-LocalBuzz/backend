package backend.airo.domain.image;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class Image {
    private final Long id;
    private String originalFilename;
    private String storedFilename;
    private String imageUrl;
    private String thumbnailUrl;
    private String altText;
    private String caption;
    private Integer fileSize;
    private String mimeType;
    private Integer width;
    private Integer height;
    private Integer sortOrder;
    private Boolean isCover;
    private LocalDateTime createdAt;



    public Image(Long id, String originalFilename, String storedFilename, String imageUrl, String thumbnailUrl, String altText, String caption, Integer fileSize, String mimeType, Integer width, Integer height, Integer sortOrder, Boolean isCover, LocalDateTime createdAt) {
        this.id = id;
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.imageUrl = imageUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.altText = altText;
        this.caption = caption;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.width = width;
        this.height = height;
        this.sortOrder = sortOrder;
        this.isCover = isCover;
        this.createdAt = createdAt;
    }


}
