package backend.airo.domain.user.command;

import backend.airo.domain.user.User;
import backend.airo.domain.user.repository.UserRepository;
import backend.airo.domain.user.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateUserCommand {

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String OAUTH2_USER_PREFIX = "oauth2:temp:";

    public User createNewUser(String token, String nickname) {
        String redisKey = OAUTH2_USER_PREFIX + token;
        Map<Object, Object> userInfo = redisTemplate.opsForHash().entries(redisKey);

        if (userInfo.isEmpty()) {
            log.error("Redis에서 사용자 정보를 찾을 수 없음 - Token: {}", token);
            throw new IllegalStateException("임시 저장된 사용자 정보를 찾을 수 없습니다.");
        }

        log.info("Redis에서 조회한 사용자 정보: {}", userInfo);

        // Redis에서 가져온 정보로 User 엔티티 생성
        User newUser = new User(
                String.valueOf(userInfo.get("email")),
                String.valueOf(userInfo.get("name")),
                nickname,
                ProviderType.valueOf(String.valueOf(userInfo.get("provider_type"))),
                String.valueOf(userInfo.get("provider_id")) // "id" 대신 "provider_id" 사용
        );

        // 사용자 저장
        User savedUser = userRepository.save(newUser);
        log.info("새로운 사용자 생성 완료 - User ID: {}, Nickname: {}", savedUser.getId(), nickname);

        // Redis에서 임시 데이터 삭제
        redisTemplate.delete(redisKey);

        return savedUser;
    }
}