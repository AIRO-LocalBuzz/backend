package backend.airo.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {

    @JsonProperty("token")
    private String token;

    @JsonProperty("nickname")
    private String nickname;
}