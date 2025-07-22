package backend.airo.domain.content.command;

import backend.airo.domain.content.repository.ContentRepository;
import backend.airo.domain.example.Test;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateContentCommand {

    private final ContentRepository contentRepository;

    public Test handle(String content) {
        return contentRepository.save(
                new Test(content)
        );
    }
}
