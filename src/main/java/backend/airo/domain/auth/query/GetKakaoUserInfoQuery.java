package backend.airo.domain.auth.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetKakaoUserInfoQuery {

    private final RestTemplate restTemplate;

    public Map<String, Object> execute(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null) {
                throw new RuntimeException("Kakao 사용자 정보를 가져올 수 없습니다.");
            }

            return transformKakaoResponse(responseBody);

        } catch (Exception e) {
            log.error("Kakao 사용자 정보 조회 실패", e);
            throw new RuntimeException("Kakao 사용자 정보 조회에 실패했습니다.", e);
        }
    }

    private Map<String, Object> transformKakaoResponse(Map<String, Object> responseBody) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", responseBody.get("id"));

        Map<String, Object> kakaoAccount = (Map<String, Object>) responseBody.get("kakao_account");
        if (kakaoAccount != null) {
            attributes.put("email", kakaoAccount.get("email"));

            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            if (profile != null) {
                attributes.put("nickname", profile.get("nickname"));
                attributes.put("profile_image_url", profile.get("profile_image_url"));
            }
        }

        return attributes;
    }
}