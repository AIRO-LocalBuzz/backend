package backend.airo.application.user.usecase;

import backend.airo.domain.user.User;
import backend.airo.domain.user.command.CreateUserCommand;
import backend.airo.domain.user.exception.UserErrorCode;
import backend.airo.domain.user.exception.UserException;
import backend.airo.domain.user.query.GetUserByFirebaseUidQuery;
import backend.airo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserUseCase userUseCase;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(
                1L,
                "firebase-uid-123",
                "test@example.com",
                "테스트유저",
                "https://example.com/avatar.jpg",
                null,
                null
        );
    }

    @Test
    @DisplayName("Firebase UID로 사용자 조회 성공")
    void getUserByFirebaseUid_Success() {
        // given
        when(userRepository.findByFirebaseUid(anyString())).thenReturn(Optional.of(testUser));

        // when
        GetUserByFirebaseUidQuery query = new GetUserByFirebaseUidQuery("firebase-uid-123");
        User result = userUseCase.getUserByFirebaseUid(query);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirebaseUid()).isEqualTo("firebase-uid-123");
        assertThat(result.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("존재하지 않는 Firebase UID로 사용자 조회시 예외 발생")
    void getUserByFirebaseUid_NotFound() {
        // given
        when(userRepository.findByFirebaseUid(anyString())).thenReturn(Optional.empty());

        // when & then
        GetUserByFirebaseUidQuery query = new GetUserByFirebaseUidQuery("nonexistent-uid");
        assertThatThrownBy(() -> userUseCase.getUserByFirebaseUid(query))
                .isInstanceOf(UserException.class)
                .hasMessage("User Layer - 사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("사용자 ID로 조회 성공")
    void getUserById_Success() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // when
        User result = userUseCase.getUserById(1L);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirebaseUid()).isEqualTo("firebase-uid-123");
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 조회시 예외 발생")
    void getUserById_NotFound() {
        // given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userUseCase.getUserById(999L))
                .isInstanceOf(UserException.class)
                .hasMessage("User Layer - 사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("사용자 생성 성공")
    void createUser_Success() {
        // given
        CreateUserCommand command = new CreateUserCommand("firebase-uid-456", "new@example.com", "신규유저", null);
        when(userRepository.existsByFirebaseUid(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(new User(2L, "firebase-uid-456", "new@example.com", "신규유저", null, null, null));

        // when
        User result = userUseCase.createUser(command);

        // then
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getFirebaseUid()).isEqualTo("firebase-uid-456");
        assertThat(result.getEmail()).isEqualTo("new@example.com");
    }
}