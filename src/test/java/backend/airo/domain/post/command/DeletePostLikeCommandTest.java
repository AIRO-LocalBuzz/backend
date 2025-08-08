package backend.airo.domain.postlike.command;

import backend.airo.domain.post.command.DeletePostLikeCommand;
import backend.airo.domain.post.exception.InvalidPostLikeException;
import backend.airo.domain.post.repository.PostLikeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeletePostLikeCommand 테스트")
class DeletePostLikeCommandTest {

    @Mock
    private PostLikeRepository postLikeRepository;

    @InjectMocks
    private DeletePostLikeCommand deletePostLikeCommand;

    private final Long VALID_POST_ID = 1L;
    private final Long VALID_USER_ID = 1L;

    @Test
    @DisplayName("TC-001: 좋아요 삭제 성공")
    void tc001_handle_ValidIds_Success() {
        // Given
        when(postLikeRepository.deleteByPostIdAndUserId(VALID_POST_ID, VALID_USER_ID))
                .thenReturn(1);

        // When
        int result = deletePostLikeCommand.handle(VALID_POST_ID, VALID_USER_ID);

        // Then
        assertThat(result).isEqualTo(1);
        verify(postLikeRepository, times(1)).deleteByPostIdAndUserId(VALID_POST_ID, VALID_USER_ID);
    }

    @Test
    @DisplayName("TC-002: 존재하지 않는 좋아요 삭제")
    void tc002_handle_NonExistentLike_ReturnsZero() {
        // Given
        Long nonExistentPostId = 999L;
        when(postLikeRepository.deleteByPostIdAndUserId(nonExistentPostId, VALID_USER_ID))
                .thenReturn(0);

        // When
        int result = deletePostLikeCommand.handle(nonExistentPostId, VALID_USER_ID);

        // Then
        assertThat(result).isEqualTo(0);
        verify(postLikeRepository, times(1)).deleteByPostIdAndUserId(nonExistentPostId, VALID_USER_ID);
    }

    @Test
    @DisplayName("TC-003: NULL postId - InvalidPostLikeException 발생")
    void tc003_handle_NullPostId_ThrowsException() {
        // Given
        Long nullPostId = null;

        // When & Then
        assertThatThrownBy(() -> deletePostLikeCommand.handle(nullPostId, VALID_USER_ID))
                .isInstanceOf(InvalidPostLikeException.class)
                .hasMessageContaining("게시물 ID는 필수입니다");

        verify(postLikeRepository, never()).deleteByPostIdAndUserId(any(), any());
    }

    @Test
    @DisplayName("TC-004: NULL userId - InvalidPostLikeException 발생")
    void tc004_handle_NullUserId_ThrowsException() {
        // Given
        Long nullUserId = null;

        // When & Then
        assertThatThrownBy(() -> deletePostLikeCommand.handle(VALID_POST_ID, nullUserId))
                .isInstanceOf(InvalidPostLikeException.class)
                .hasMessageContaining("사용자 ID는 필수입니다");

        verify(postLikeRepository, never()).deleteByPostIdAndUserId(any(), any());
    }

    @Test
    @DisplayName("TC-005: 모든 파라미터 NULL - InvalidPostLikeException 발생")
    void tc005_handle_AllNullParams_ThrowsException() {
        // Given
        Long nullPostId = null;
        Long nullUserId = null;

        // When & Then
        assertThatThrownBy(() -> deletePostLikeCommand.handle(nullPostId, nullUserId))
                .isInstanceOf(InvalidPostLikeException.class)
                .hasMessageContaining("게시물 ID는 필수입니다");

        verify(postLikeRepository, never()).deleteByPostIdAndUserId(any(), any());
    }

    @Test
    @DisplayName("TC-006: 음수 postId - InvalidPostLikeException 발생")
    void tc006_handle_NegativePostId_ThrowsException() {
        // Given
        Long negativePostId = -1L;

        // When & Then
        assertThatThrownBy(() -> deletePostLikeCommand.handle(negativePostId, VALID_USER_ID))
                .isInstanceOf(InvalidPostLikeException.class)
                .hasMessageContaining("게시물 ID는 양수여야 합니다");

        verify(postLikeRepository, never()).deleteByPostIdAndUserId(any(), any());
    }

    @Test
    @DisplayName("TC-007: 음수 userId - InvalidPostLikeException 발생")
    void tc007_handle_NegativeUserId_ThrowsException() {
        // Given
        Long negativeUserId = -1L;

        // When & Then
        assertThatThrownBy(() -> deletePostLikeCommand.handle(VALID_POST_ID, negativeUserId))
                .isInstanceOf(InvalidPostLikeException.class)
                .hasMessageContaining("사용자 ID는 양수여야 합니다");

        verify(postLikeRepository, never()).deleteByPostIdAndUserId(any(), any());
    }

    @Test
    @DisplayName("TC-008: Repository 예외 발생")
    void tc008_handle_RepositoryException_ThrowsException() {
        // Given
        when(postLikeRepository.deleteByPostIdAndUserId(VALID_POST_ID, VALID_USER_ID))
                .thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThatThrownBy(() -> deletePostLikeCommand.handle(VALID_POST_ID, VALID_USER_ID))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database error");

        verify(postLikeRepository, times(1)).deleteByPostIdAndUserId(VALID_POST_ID, VALID_USER_ID);
    }

    @Test
    @DisplayName("TC-009: 0값 ID - InvalidPostLikeException 발생")
    void tc009_handle_ZeroIds_ThrowsException() {
        // Given
        Long zeroPostId = 0L;
        Long zeroUserId = 0L;

        // When & Then
        assertThatThrownBy(() -> deletePostLikeCommand.handle(zeroPostId, VALID_USER_ID))
                .isInstanceOf(InvalidPostLikeException.class)
                .hasMessageContaining("게시물 ID는 양수여야 합니다");

        assertThatThrownBy(() -> deletePostLikeCommand.handle(VALID_POST_ID, zeroUserId))
                .isInstanceOf(InvalidPostLikeException.class)
                .hasMessageContaining("사용자 ID는 양수여야 합니다");

        verify(postLikeRepository, never()).deleteByPostIdAndUserId(any(), any());
    }
}