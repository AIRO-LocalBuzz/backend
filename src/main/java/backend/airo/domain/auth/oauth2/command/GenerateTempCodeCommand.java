package backend.airo.domain.auth.oauth2.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenerateTempCodeCommand {

    private final RedisTemplate<String, String> redisTemplate;

    public String generate(Long userId) {
        // 임시 코드 생성
        String tempCode = UUID.randomUUID().toString();
        log.info("임시 인증 코드 생성: {}", tempCode);

        // Redis에 저장 (1분 유효)
        redisTemplate.opsForValue().set(
                "auth_code:" + tempCode,
                userId.toString(),
                1, TimeUnit.MINUTES
        );
        log.info("Redis에 임시 코드 저장 완료 - User ID: {}", userId);

        return tempCode;
    }
}