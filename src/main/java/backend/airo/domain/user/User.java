package backend.airo.domain.user;

import lombok.Getter;

@Getter
public class User {

    private Long id = 0L;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String phoneNumber;
    private UserRole role;
    private UserStatus status;

    public User(String username, String email, String password, String fullName, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.role = UserRole.USER;
        this.status = UserStatus.ACTIVE;
    }

    public User(Long id, String username, String email, String password, String fullName, String phoneNumber, UserRole role, UserStatus status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
    }
}