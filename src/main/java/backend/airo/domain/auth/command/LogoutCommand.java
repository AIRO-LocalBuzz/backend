package backend.airo.domain.auth.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogoutCommand {

    private final RedisTemplate<String, String> redisTemplate;

    public void execute(String accessToken, Long userId) {
        log.info("로그아웃 처리 - User ID: {}", userId);

        // 리프레시 토큰 삭제
        redisTemplate.delete("RT:" + userId);

        // 액세스 토큰 블랙리스트 추가
        redisTemplate.opsForValue().set(
                "BL:" + accessToken,
                "logout",
                30,
                TimeUnit.MINUTES
        );

        log.info("로그아웃 완료 - User ID: {}", userId);
    }
}