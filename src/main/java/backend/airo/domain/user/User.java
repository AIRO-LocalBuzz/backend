package backend.airo.domain.user;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class User {

    private Long id;
    private String nickname;
    private boolean firstLoginCompleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(String nickname) {
        this.nickname = nickname;
        this.firstLoginCompleted = true; // Setting nickname completes first login
    }

    public User(Long id, String nickname, boolean firstLoginCompleted) {
        this.id = id;
        this.nickname = nickname;
        this.firstLoginCompleted = firstLoginCompleted;
    }

    public User(Long id, String nickname, boolean firstLoginCompleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nickname = nickname;
        this.firstLoginCompleted = firstLoginCompleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User updateNickname(String nickname) {
        return new User(this.id, nickname, true, this.createdAt, this.updatedAt);
    }

    public boolean isFirstLogin() {
        return !firstLoginCompleted;
    }
}