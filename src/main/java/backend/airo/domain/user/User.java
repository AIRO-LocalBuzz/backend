package backend.airo.domain.user;

import backend.airo.domain.user.enums.ProviderType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

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

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public User(String email, String name, String nickname, String phoneNumber, LocalDate birthDate, ProviderType providerType, String providerId) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.providerType = providerType;
        this.providerId = providerId;
    }

    public User(String email, String nickname, ProviderType providerType, String providerId){
        this.email = email;
        this.nickname = nickname;
        this.providerType = providerType;
        this.providerId = providerId;
    }

    public void updateProfile(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
    }
}