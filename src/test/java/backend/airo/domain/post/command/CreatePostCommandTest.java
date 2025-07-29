package backend.airo.domain.post.command;

import backend.airo.domain.post.enums.PostStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CreatePostCommandTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("유효한 게시물 생성 커맨드 테스트")
    void validCreatePostCommand() {
        // given
        CreatePostCommand command = new CreatePostCommand(
                "여행 후기", "정말 좋은 여행이었습니다", 1L, PostStatus.DRAFT,
                1, 1L, null, List.of(), List.of(), false
        );

        // when
        Set<ConstraintViolation<CreatePostCommand>> violations = validator.validate(command);

        // then
        assertThat(violations).isEmpty();
        assertThat(command.hasImages()).isFalse();
        assertThat(command.hasTags()).isFalse();
    }

    @Test
    @DisplayName("제목 누락 시 검증 실패")
    void invalidCreatePostCommandWithoutTitle() {
        // given
        CreatePostCommand command = new CreatePostCommand(
                "", "내용", 1L, PostStatus.DRAFT,
                null, null, null, List.of(), List.of(), false
        );

        // when
        Set<ConstraintViolation<CreatePostCommand>> violations = validator.validate(command);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("제목은 필수입니다");
    }

    @Test
    @DisplayName("발행 가능 여부 검증")
    void canPublishValidation() {
        // given
        CreatePostCommand publishCommand = CreatePostCommand.forPublish(
                "제목", "내용", 1L, 1, 1L
        );
        CreatePostCommand draftCommand = CreatePostCommand.forDraft(
                "제목", "내용", 1L
        );

        // when & then
        assertThat(publishCommand.canPublish()).isTrue();
        assertThat(draftCommand.canPublish()).isFalse();
    }
}