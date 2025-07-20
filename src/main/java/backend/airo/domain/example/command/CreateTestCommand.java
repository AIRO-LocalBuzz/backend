package backend.airo.domain.example.command;

import backend.airo.domain.example.Test;
import backend.airo.domain.example.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateTestCommand {

    private final TestRepository testRepository;

    public Test handle(String content) {
        return testRepository.save(
                new Test(content)
        );
    }
}
