package backend.airo.domain.content.query;

import backend.airo.domain.content.repository.ContentRepository;
import backend.airo.domain.example.Test;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetContentQuery {

    private final ContentRepository contentRepository;

    public Test handle(Long testId) {
        return contentRepository.findById(
                testId
        );
    }

}
