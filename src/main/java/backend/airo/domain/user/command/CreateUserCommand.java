package backend.airo.domain.user.command;

import lombok.Getter;

@Getter
public class CreateUserCommand {
    private final String firebaseUid;
    private final String email;
    private final String nickname;
    private final String profileImageUrl;

    public CreateUserCommand(String firebaseUid, String email, String nickname, String profileImageUrl) {
        this.firebaseUid = firebaseUid;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}