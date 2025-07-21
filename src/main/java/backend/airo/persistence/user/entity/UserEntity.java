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

    @Column(unique = true, length = 20, nullable = false)
    private String nickname;

    @Column(name = "first_login_completed", nullable = false)
    private boolean firstLoginCompleted = false;

    public UserEntity(String nickname) {
        this.nickname = nickname;
        this.firstLoginCompleted = true; // Setting nickname completes first login
    }

    public UserEntity(String nickname, boolean firstLoginCompleted) {
        this.nickname = nickname;
        this.firstLoginCompleted = firstLoginCompleted;
    }

    public static UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity(user.getNickname(), user.isFirstLoginCompleted());
        return entity;
    }

    public static User toDomain(UserEntity entity) {
        return new User(
            entity.getId(), 
            entity.getNickname(), 
            entity.isFirstLoginCompleted(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
        this.firstLoginCompleted = true;
    }
}