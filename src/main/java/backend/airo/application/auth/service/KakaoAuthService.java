package backend.airo.application.auth.service;

import backend.airo.api.auth.dto.KakaoUserInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoAuthService {

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    @Value("${social.kakao.client-id}")
    private String clientId;

    public KakaoUserInfo getUserInfo(String accessToken) {
        try {
            WebClient webClient = webClientBuilder.build();

            String response = webClient.get()
                    .uri("https://kapi.kakao.com/v2/user/me")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return parseKakaoUserInfo(response);
        } catch (WebClientResponseException e) {
            log.error("Failed to get Kakao user info: {}", e.getMessage());
            throw new RuntimeException("Failed to get Kakao user info", e);
        }
    }

    private KakaoUserInfo parseKakaoUserInfo(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            
            String id = jsonNode.path("id").asText();
            JsonNode kakaoAccount = jsonNode.path("kakao_account");
            JsonNode profile = kakaoAccount.path("profile");
            
            String email = kakaoAccount.path("email").asText();
            String nickname = profile.path("nickname").asText();
            String profileImageUrl = profile.path("profile_image_url").asText();

            return new KakaoUserInfo(id, email, nickname, profileImageUrl);
        } catch (Exception e) {
            log.error("Failed to parse Kakao user info", e);
            throw new RuntimeException("Failed to parse Kakao user info", e);
        }
    }
}