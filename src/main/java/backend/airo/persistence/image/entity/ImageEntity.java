package backend.airo.persistence.image.entity;

import backend.airo.domain.image.Image;
import backend.airo.persistence.abstracts.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "post_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class  ImageEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "post_id", nullable = true)
    private Long postId;

    @Column(name = "original_filename", nullable = true)
    private String originalFilename;

    @Column(name = "stored_filename", nullable = true)
    private String storedFilename;

    @Column(name = "image_url", columnDefinition = "TEXT", nullable = false)
    private String imageUrl;

    @Column(name = "alt_text")
    private String altText;

    @Column(columnDefinition = "TEXT")
    private String caption;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    private Integer width;
    private Integer height;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @Column(name = "is_cover", nullable = false)
    private Boolean isCover = false;



    public ImageEntity(Long userId, Long postId, String originalFilename, String storedFilename,
                       String imageUrl, String altText, String caption, Long fileSize, String mimeType,
                       Integer width, Integer height, Integer sortOrder, Boolean isCover ) {
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

    public static ImageEntity toEntity(Image image) {
        return new ImageEntity(
                image.getUserId(),
                image.getPostId(),
                image.getOriginalFilename(),
                image.getStoredFilename(),
                image.getImageUrl(),
                image.getAltText(),
                image.getCaption(),
                image.getFileSize(),
                image.getMimeType(),
                image.getWidth(),
                image.getHeight(),
                image.getSortOrder(),
                image.getIsCover()
        );
    }

    public static Image toDomain(ImageEntity image) {
        return new Image(
                image.getId(),
                image.getUserId(),
                image.getPostId(),
                image.getOriginalFilename(),
                image.getStoredFilename(),
                image.getImageUrl(),
                image.getAltText(),
                image.getCaption(),
                image.getFileSize(),
                image.getMimeType(),
                image.getWidth(),
                image.getHeight(),
                image.getSortOrder(),
                image.getIsCover()
        );
    }


}
