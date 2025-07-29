package backend.airo.persistence.category.entity;

import backend.airo.domain.post.Post;
import backend.airo.persistence.post.entity.PostEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false, length = 100)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "color_code", length = 7)
    private String colorCode;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 연관관계
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<PostEntity> posts = new ArrayList<>();
//
//    // 기본 생성자
//    public Category() {}
//
//    // 생성자
//    public Category(String name, String slug) {
//        this.name = name;
//        this.slug = slug;
//    }
}
