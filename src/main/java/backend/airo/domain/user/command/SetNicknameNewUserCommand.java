package backend.airo.domain.user.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SetNicknameNewUserCommand {

//    private final UserRepository userRepository;
//
//    public User execute(Long userId, String nickname) {
//        log.info("사용자 닉네임 설정 - User ID: {}, Nickname: {}", userId, nickname);
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
//
//        user.setNickname(nickname);
//        User savedUser = userRepository.save(user);
//
//        log.info("닉네임 설정 완료 - User ID: {}, Nickname: {}", savedUser.getId(), savedUser.getNickname());
//
//        return savedUser;
//    }
}