package backend.airo.api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckNicknameResponse {
    private boolean available;
    private String message;

    public static CheckNicknameResponse available() {
        return new CheckNicknameResponse(true, "사용 가능한 닉네임입니다.");
    }

    public static CheckNicknameResponse unavailable() {
        return new CheckNicknameResponse(false, "이미 사용중인 닉네임입니다.");
    }

    public static CheckNicknameResponse invalid() {
        return new CheckNicknameResponse(false, "유효하지 않은 닉네임입니다.");
    }
}