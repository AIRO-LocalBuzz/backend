package backend.airo.domain.user.command;

import backend.airo.domain.user.User;
import backend.airo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import backend.airo.api.user.dto.UpdateUserInfoRequest;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateExistingUserCommand {

    private final UserRepository userRepository;

    public User execute(User existingUser, UpdateUserInfoRequest updateUserInfoRequest) {
        log.info("기존 사용자 정보 업데이트 - User ID: {}", existingUser.getId());

        // OAuth2에서 받은 최신 정보로 업데이트
        existingUser.updateUserInfo(
                updateUserInfoRequest.getEmail(),
                updateUserInfoRequest.getNickname()
        );

        User updatedUser = userRepository.save(existingUser);
        log.info("사용자 정보 업데이트 완료 - User ID: {}", updatedUser.getId());

        return updatedUser;
    }
}