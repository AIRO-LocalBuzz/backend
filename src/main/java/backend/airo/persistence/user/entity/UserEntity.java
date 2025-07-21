package backend.airo.persistence.user.entity;

import backend.airo.domain.user.User;
import backend.airo.domain.user.UserRole;
import backend.airo.domain.user.UserStatus;
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
    private Long id = 0L;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status;

    public UserEntity(String username, String email, String password, String fullName, String phoneNumber, UserRole role, UserStatus status) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
    }

    public static UserEntity toEntity(User user) {
        return new UserEntity(
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            user.getFullName(),
            user.getPhoneNumber(),
            user.getRole(),
            user.getStatus()
        );
    }

    public static User toDomain(UserEntity userEntity) {
        return new User(
            userEntity.getId(),
            userEntity.getUsername(),
            userEntity.getEmail(),
            userEntity.getPassword(),
            userEntity.getFullName(),
            userEntity.getPhoneNumber(),
            userEntity.getRole(),
            userEntity.getStatus()
        );
    }
}