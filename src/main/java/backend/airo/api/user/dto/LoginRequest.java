package backend.airo.api.user.dto;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String idToken;

    public LoginRequest() {}

    public LoginRequest(String idToken) {
        this.idToken = idToken;
    }
}