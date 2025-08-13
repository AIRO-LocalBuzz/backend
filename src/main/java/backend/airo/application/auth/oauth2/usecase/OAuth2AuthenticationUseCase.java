package backend.airo.application.auth.oauth2.usecase;

import backend.airo.domain.auth.oauth2.command.GenerateTempCodeCommand;
import backend.airo.domain.auth.oauth2.query.FindOAuth2UserQuery;
import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationUseCase {

    private final FindOAuth2UserQuery findOAuth2UserQuery;
    private final GenerateTempCodeCommand generateTempCodeCommand;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String OAUTH2_USER_PREFIX = "oauth2:temp:";
    private static final long REDIS_TTL_MINUTES = 30;

    @Value("${app.oauth2.success-url:https://site-navy-six-67.vercel.app/auth/success}")
    private String successBaseUrl;

    @Value("${app.oauth2.failure-url:https://site-navy-six-67.vercel.app/auth/nickname}")
    private String failureUrl;

    public String handleAuthenticationSuccess(OAuth2User oauth2User, String accessToken) {
        // 1. OAuth2 정보 추출
        String providerId = oauth2User.getAttribute("provider_id");
        ProviderType providerType = oauth2User.getAttribute("provider_type");

        log.info("Provider ID: {}, Provider Type: {}", providerId, providerType);

        // 2. 사용자 조회
        Optional<User> userOptional = findOAuth2UserQuery.findByProviderIdAndType(providerId, providerType);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            log.info("기존 사용자 찾음 - User ID: {}, Email: {}", user.getId(), user.getEmail());

            // 3. 토큰 저장
            generateTempCodeCommand.generate(user.getId(), accessToken);

            // 4. 성공 URL 반환
            return successBaseUrl + "?token=" + accessToken;

        } else {
            log.warn("사용자를 찾을 수 없음 - Provider ID: {}, Provider Type: {}", providerId, providerType);
            // OAuth2User 속성들을 Redis에 저장
            saveOAuth2UserToRedis(accessToken, oauth2User);
            return failureUrl + "?token=" + accessToken;
        }
    }

    private void saveOAuth2UserToRedis(String accessToken, OAuth2User oauth2User) {
        String redisKey = OAUTH2_USER_PREFIX + accessToken;
        Map<String, Object> attributes = new HashMap<>();

        oauth2User.getAttributes().forEach((key, value) -> {
            if (value != null) {
                // provider_id로 저장
                if (key.equals("id")) {
                    attributes.put("provider_id", value.toString());
                } else {
                    attributes.put(key, value.toString());
                }
            }
        });

        try {
            redisTemplate.opsForHash().putAll(redisKey, attributes);
            redisTemplate.expire(redisKey, REDIS_TTL_MINUTES, TimeUnit.MINUTES);
            log.info("OAuth2 사용자 정보 Redis 저장 완료 - Access Token: {}", accessToken);
        } catch (Exception e) {
            log.error("OAuth2 사용자 정보 Redis 저장 실패 - Access Token: {}, Error: {}", accessToken, e.getMessage());
        }
    }
}
