package backend.airo.integration;

import backend.airo.application.user.usecase.UserUseCase;
import backend.airo.domain.user.User;
import backend.airo.domain.user.command.CreateUserCommand;
import backend.airo.domain.user.query.GetUserByFirebaseUidQuery;
import backend.airo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class UserIntegrationTest {

    @Autowired
    private UserUseCase userUseCase;

    @MockBean
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자 생성 통합 테스트")
    void createUser_Integration_Success() {
        // given
        CreateUserCommand command = new CreateUserCommand(
            "firebase-uid-integration",
            "integration@test.com",
            "통합테스트유저",
            "https://example.com/avatar.jpg"
        );

        User savedUser = new User(
            1L,
            "firebase-uid-integration",
            "integration@test.com",
            "통합테스트유저",
            "https://example.com/avatar.jpg",
            null,
            null
        );

        when(userRepository.existsByFirebaseUid(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // when
        User result = userUseCase.createUser(command);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirebaseUid()).isEqualTo("firebase-uid-integration");
        assertThat(result.getEmail()).isEqualTo("integration@test.com");
        assertThat(result.getNickname()).isEqualTo("통합테스트유저");
        assertThat(result.getProfileImageUrl()).isEqualTo("https://example.com/avatar.jpg");
    }

    @Test
    @DisplayName("Firebase UID로 사용자 조회 통합 테스트")
    void getUserByFirebaseUid_Integration_Success() {
        // given
        User existingUser = new User(
            2L,
            "firebase-uid-query",
            "query@test.com",
            "조회테스트유저",
            null,
            null,
            null
        );

        when(userRepository.findByFirebaseUid("firebase-uid-query")).thenReturn(Optional.of(existingUser));

        // when
        GetUserByFirebaseUidQuery query = new GetUserByFirebaseUidQuery("firebase-uid-query");
        User result = userUseCase.getUserByFirebaseUid(query);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getFirebaseUid()).isEqualTo("firebase-uid-query");
        assertThat(result.getEmail()).isEqualTo("query@test.com");
        assertThat(result.getNickname()).isEqualTo("조회테스트유저");
    }
}