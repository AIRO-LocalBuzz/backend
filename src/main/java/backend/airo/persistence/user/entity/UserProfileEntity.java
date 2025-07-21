package backend.airo.persistence.user.entity;

import backend.airo.domain.user.UserProfile;
import backend.airo.persistence.abstracts.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "user_profile")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserProfileEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    @Column(length = 100)
    private String name;

    @Column(length = 20)
    private String contact;

    private LocalDate birthDate;

    @Column(length = 255)
    private String email;

    public UserProfileEntity(String name, String contact, LocalDate birthDate, String email) {
        this.name = name;
        this.contact = contact;
        this.birthDate = birthDate;
        this.email = email;
    }

    public UserProfileEntity(Long id, String name, String contact, LocalDate birthDate, String email) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.birthDate = birthDate;
        this.email = email;
    }

    public static UserProfileEntity toEntity(UserProfile userProfile) {
        return new UserProfileEntity(
                userProfile.getName(),
                userProfile.getContact(),
                userProfile.getBirthDate(),
                userProfile.getEmail()
        );
    }

    public static UserProfile toDomain(UserProfileEntity entity) {
        return new UserProfile(
                entity.getId(),
                entity.getName(),
                entity.getContact(),
                entity.getBirthDate(),
                entity.getEmail()
        );
    }

    public UserProfileEntity update(String name, String contact, LocalDate birthDate, String email) {
        this.name = name;
        this.contact = contact;
        this.birthDate = birthDate;
        this.email = email;
        return this;
    }
}