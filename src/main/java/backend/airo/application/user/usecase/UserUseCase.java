package backend.airo.application.user.usecase;

import backend.airo.domain.user.User;
import backend.airo.domain.user.command.UpdateExistingUserCommand;
import backend.airo.domain.user.query.GetUserQuery;
import backend.airo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class UserUseCase {

    private final GetUserQuery getUserCommand;
    private final UpdateExistingUserCommand updateExistingUserCommand;
    private final UserRepository userRepository;

    public User getUserById(Long userId) {
        return getUserCommand.handle(userId);
    }

    public User updateUser(Long userId, String name, String nickname, String phoneNumber, LocalDate birthDate) {
        return updateExistingUserCommand.handle(userId, name, nickname, phoneNumber, birthDate);
    }

    public void deleteUser(Long userId) {
        // User 존재 여부 확인
        User user = userRepository.findById(userId);
        
        // User와 연관된 모든 데이터 삭제
        userRepository.deleteUserWithRelatedData(userId);
    }

}
