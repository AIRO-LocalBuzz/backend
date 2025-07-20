package backend.airo.domain.example.query;

import backend.airo.domain.example.Test;
import backend.airo.domain.example.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetTestQuery {

    private final TestRepository testRepository;

    public Test handle(Long testId) {
        return testRepository.findById(
                testId
        );
    }

}
