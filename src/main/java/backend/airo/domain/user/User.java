package backend.airo.domain.user;

import backend.airo.domain.user.enums.ProviderType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class User {
    private final Long id;
    private String email;
    private String name;
    private String nickname;
    private String phoneNumber;
    private LocalDate birthDate;
    private final ProviderType providerType;
    private final String providerId;
    private String bio;
    private String profileImageUrl;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;



    public User(Long id, String email, String name, String nickname, String phoneNumber, LocalDate birthDate, ProviderType providerType, String providerId) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.providerType = providerType;
        this.providerId = providerId;
    }

    public User(String email, String name, String nickname, ProviderType providerType, String providerId) {
        this.id = null;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.providerType = providerType;
        this.providerId = providerId;
    }

    public User(String email, String name, ProviderType providerType, String providerId) {
        this.id = null;
        this.email = email;
        this.name = name;
        this.providerType = providerType;
        this.providerId = providerId;
    }


    public static User createNewUser(String email, String name, ProviderType providerType, String providerId) {
        return new User(
                email,
                name != null ? name : email, // name이 null이면 email을 nickname으로 사용
                providerType,
                providerId
        );
    }

    public void updateUserInfo(String email, String nickname) {
            this.email = email;
            this.nickname = nickname;

    }
}
