package backend.airo.domain.auth.oauth2.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenerateTempCodeCommand {

    private final RedisTemplate<String, String> redisTemplate;

    public void generate(Long userId, String accessToken) {

        // Redis에 저장 (1분 유효)
        redisTemplate.opsForValue().set(
                "auth_code:" + accessToken,
                userId.toString(),
                5, TimeUnit.MINUTES
        );
        log.info("Redis에 토큰 저장 완료 - User ID: {}", userId);

    }
}