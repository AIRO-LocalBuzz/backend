package backend.airo.persistence.user.entity;

import backend.airo.domain.user.User;
import backend.airo.persistence.abstracts.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firebase_uid", nullable = false, unique = true)
    private String firebaseUid;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    public UserEntity(String firebaseUid, String email, String nickname, String profileImageUrl) {
        this.firebaseUid = firebaseUid;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public static UserEntity fromDomain(User user) {
        return new UserEntity(user.getFirebaseUid(), user.getEmail(), user.getNickname(), user.getProfileImageUrl());
    }

    public User toDomain() {
        return new User(this.id, this.firebaseUid, this.email, this.nickname, this.profileImageUrl, this.getCreatedAt(), this.getUpdatedAt());
    }

    public void updateProfile(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}