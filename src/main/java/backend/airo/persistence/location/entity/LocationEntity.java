package backend.airo.persistence.location.entity;

import backend.airo.domain.post.Post;
import backend.airo.persistence.post.entity.PostEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "location")
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String country;

    @Column(name = "state_province", length = 100)
    private String stateProvince;

    @Column(length = 100)
    private String city;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "google_place_id")
    private String googlePlaceId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 연관관계
    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    private List<PostEntity> posts = new ArrayList<>();

//    // 기본 생성자
//    public Location() {}
//
//    // 생성자
//    public Location(String country, String city, BigDecimal latitude, BigDecimal longitude) {
//        this.country = country;
//        this.city = city;
//        this.latitude = latitude;
//        this.longitude = longitude;
//    }
}
