package backend.airo.domain.auth.oauth2.query;

import backend.airo.domain.user.User;
import backend.airo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GetUserByTempCodeQuery {

    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;

    public User getUserByTempCode(String tempCode) {
        log.info("토큰 교환 요청 - 임시 코드: {}", tempCode);

        // Redis에서 임시 코드로 사용자 ID 조회
        String userIdStr = redisTemplate.opsForValue().get("auth_code:" + tempCode);

        if (userIdStr == null) {
            log.warn("유효하지 않거나 만료된 인증 코드: {}", tempCode);
            throw new IllegalArgumentException("유효하지 않거나 만료된 인증 코드입니다.");
        }

        Long userId = Long.parseLong(userIdStr);
        log.info("Redis에서 사용자 ID 조회 성공: {}", userId);

        // 사용자 정보 조회
        return userRepository.findById(userId);
    }
}
