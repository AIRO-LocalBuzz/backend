package backend.airo.persistence.image.entity;

import backend.airo.domain.image.Image;
import backend.airo.domain.post.Post;
import backend.airo.persistence.post.entity.PostEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "post_images")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Column(name = "stored_filename", nullable = false)
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

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    // 기본 생성자
    public ImageEntity() {
    }

    public ImageEntity(Long userId, String originalFilename, String storedFilename, String imageUrl, String altText, String caption, Long fileSize, String mimeType, Integer width, Integer height, Integer sortOrder, Boolean isCover, LocalDateTime createdAt, Post post) {
        super();
    }

    public static ImageEntity toEntity(Image image) {
        return new ImageEntity(
                image.getUserId(),
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
                image.getIsCover(),
                image.getCreatedAt(),
                image.getPost()
        );
    }

    public static Image toDomain(ImageEntity image) {
        return new Image(
                image.getId(),
                image.getUserId(),
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
                image.getIsCover(),
                image.getCreatedAt(),
                PostEntity.toDomain(image.getPost())
        );
    }


}
