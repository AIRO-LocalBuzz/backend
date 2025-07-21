package backend.airo.domain.user;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class User {

    private Long id;
    private String firebaseUid;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(String firebaseUid, String email, String nickname, String profileImageUrl) {
        this.firebaseUid = firebaseUid;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public User(Long id, String firebaseUid, String email, String nickname, String profileImageUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.firebaseUid = firebaseUid;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateProfile(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}