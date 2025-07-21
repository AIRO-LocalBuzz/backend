package backend.airo.application.user.usecase;

import backend.airo.domain.user.User;
import backend.airo.domain.user.command.CreateUserCommand;
import backend.airo.domain.user.exception.UserErrorCode;
import backend.airo.domain.user.exception.UserException;
import backend.airo.domain.user.query.GetUserByFirebaseUidQuery;
import backend.airo.domain.user.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserUseCase {

    private final UserRepository userRepository;

    /**
     * Firebase ID 토큰을 검증하고, 신규 사용자인 경우 자동으로 회원가입 처리
     */
    public User loginWithFirebase(String idToken) {
        try {
            // Firebase ID 토큰 검증
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String firebaseUid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            String name = decodedToken.getName();
            String pictureUrl = decodedToken.getPicture();

            log.info("Firebase 토큰 검증 성공: uid={}, email={}", firebaseUid, email);

            // 기존 사용자 조회
            Optional<User> existingUser = userRepository.findByFirebaseUid(firebaseUid);
            
            if (existingUser.isPresent()) {
                log.info("기존 사용자 로그인: uid={}", firebaseUid);
                return existingUser.get();
            }

            // 신규 사용자 자동 회원가입
            log.info("신규 사용자 자동 회원가입: uid={}, email={}", firebaseUid, email);
            String nickname = name != null ? name : generateNicknameFromEmail(email);
            
            CreateUserCommand command = new CreateUserCommand(firebaseUid, email, nickname, pictureUrl);
            return createUser(command);

        } catch (FirebaseAuthException e) {
            log.error("Firebase 토큰 검증 실패: {}", e.getMessage());
            throw new UserException(UserErrorCode.INVALID_FIREBASE_TOKEN);
        }
    }

    /**
     * 사용자 생성
     */
    public User createUser(CreateUserCommand command) {
        // 중복 검사
        if (userRepository.existsByFirebaseUid(command.getFirebaseUid())) {
            throw new UserException(UserErrorCode.USER_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(command.getEmail())) {
            throw new UserException(UserErrorCode.USER_ALREADY_EXISTS);
        }

        User newUser = new User(
            command.getFirebaseUid(),
            command.getEmail(),
            command.getNickname(),
            command.getProfileImageUrl()
        );

        User savedUser = userRepository.save(newUser);
        log.info("신규 사용자 생성 완료: id={}, firebaseUid={}", savedUser.getId(), savedUser.getFirebaseUid());
        
        return savedUser;
    }

    /**
     * Firebase UID로 사용자 조회
     */
    @Transactional(readOnly = true)
    public User getUserByFirebaseUid(GetUserByFirebaseUidQuery query) {
        return userRepository.findByFirebaseUid(query.getFirebaseUid())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    /**
     * 사용자 ID로 조회
     */
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    /**
     * 이메일에서 닉네임 생성 (예: test@gmail.com -> test)
     */
    private String generateNicknameFromEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "사용자";
        }
        return email.substring(0, email.indexOf("@"));
    }
}