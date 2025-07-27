package backend.airo.api.user.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateUserInfoRequest {
    private final String nickname;
    private final String email;

}
