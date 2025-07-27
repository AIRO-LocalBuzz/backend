package backend.airo.persistence.user.entity;

import backend.airo.persistence.abstracts.BaseEntity;
import backend.airo.domain.user.enums.ProviderType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import backend.airo.domain.user.User;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type", nullable = false)
    private ProviderType providerType;

    @Column(name = "provider_id", nullable = false, unique = true)
    private String providerId;

    @Column(name = "bio", length = 500)
    private String bio;

    @Column(name = "profile_image_url")
    private String profileImageUrl;


    public UserEntity(String email, String name, String nickname, String phoneNumber, LocalDate birthDate, ProviderType providerType, String providerId) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.providerType = providerType;
        this.providerId = providerId;
    }

    public UserEntity(String email, String nickname, ProviderType providerType, String providerId){
        this.email = email;
        this.nickname = nickname;
        this.providerType = providerType;
        this.providerId = providerId;
    }


    public static UserEntity createOAuth2User(String email, String name, ProviderType providerType, String providerId) {
        return new UserEntity(
                email,
                name != null ? name : email, // name이 null이면 email을 nickname으로 사용
                providerType,
                providerId
        );
    }

    public void updateProfile(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
    }



    public static UserEntity toEntity(User user) {
        return new UserEntity(
                user.getEmail(),
                user.getName(),
                user.getNickname(),
                user.getPhoneNumber(),
                user.getBirthDate(),
                user.getProviderType(),
                user.getProviderId()
        );
    }

    public static User toDomain(UserEntity user) {

        return new User(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getNickname(),
                user.getPhoneNumber(),
                user.getBirthDate(),
                user.getProviderType(),
                user.getProviderId()
        );


    }
}