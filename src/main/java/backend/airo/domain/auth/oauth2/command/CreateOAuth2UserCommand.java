package backend.airo.domain.auth.oauth2.command;

import backend.airo.domain.auth.oauth2.OAuth2UserInfo;
import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;
import backend.airo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateOAuth2UserCommand {
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String REDIS_KEY_PREFIX = "user:";
    private static final long REDIS_TTL_MINUTES = 30;

    public User execute(Optional<User> existingUser, OAuth2UserInfo oauth2UserInfo, ProviderType providerType) {
//        if (existingUser.isPresent()) {
//            return existingUser.get();
//        }

//        User newUser = createNewUser(oauth2UserInfo, providerType);
//
//        if (!existingUser.isPresent()) {
//            saveToRedis(newUser);
//        }
//        throw new RedirectToNicknameException(newUser.getId());
        return existingUser.orElseGet(() -> createNewUser(oauth2UserInfo, providerType));
    }

    private void saveToRedis(User user) {
        String redisKey = REDIS_KEY_PREFIX + user.getId();
        Map<String, Object> userInfo = new HashMap<>();

        userInfo.put("email", user.getEmail());
        userInfo.put("name", user.getName());
        userInfo.put("provider_id", user.getProviderId());
        userInfo.put("provider_type", user.getProviderType().toString());

        try {
            redisTemplate.opsForHash().putAll(redisKey, userInfo);
            redisTemplate.expire(redisKey, REDIS_TTL_MINUTES, TimeUnit.MINUTES);
            log.info("사용자 정보 Redis 저장 완료 - User ID: {}", user.getId());
        } catch (Exception e) {
            log.error("Redis 저장 실패 - User ID: {}, Error: {}", user.getId(), e.getMessage());
        }
    }

    private User createNewUser(OAuth2UserInfo oauth2UserInfo, ProviderType providerType) {
        log.info("새로운 OAuth2 사용자 생성 - Email: {}, Provider: {}", oauth2UserInfo.getEmail(), providerType);

        User newUser = User.createNewUser(
                oauth2UserInfo.getEmail(),
                oauth2UserInfo.getName(),
                providerType,
                oauth2UserInfo.getId()
        );

//        User savedUser = userRepository.save(newUser);
//        log.info("새로운 사용자 생성 완료 - User ID: {}", savedUser.getId());

        return newUser;
    }


}