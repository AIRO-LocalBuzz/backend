package backend.airo.domain.user;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserProfile {

    private Long id = 0L;
    private String name;
    private String contact;
    private LocalDate birthDate;
    private String email;

    public UserProfile(String name, String contact, LocalDate birthDate, String email) {
        this.name = name;
        this.contact = contact;
        this.birthDate = birthDate;
        this.email = email;
    }

    public UserProfile(Long id, String name, String contact, LocalDate birthDate, String email) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.birthDate = birthDate;
        this.email = email;
    }

    public UserProfile updateProfile(String name, String contact, LocalDate birthDate, String email) {
        return new UserProfile(this.id, name, contact, birthDate, email);
    }
}