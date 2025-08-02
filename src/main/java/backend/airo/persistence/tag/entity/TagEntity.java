package backend.airo.persistence.tag.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "tags")
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false, length = 100)
    private String slug;

    @Column(name = "usage_count", nullable = false)
    private Integer usageCount = 0;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


//    // 기본 생성자
//    public Tag() {}
//
//    // 생성자
//    public Tag(String name, String slug) {
//        this.name = name;
//        this.slug = slug;
//    }
//
//    // 비즈니스 메서드
//    public void incrementUsageCount() {
//        this.usageCount++;
//    }
//
//    public void decrementUsageCount() {
//        if (this.usageCount > 0) {
//            this.usageCount--;
//        }
//    }

}