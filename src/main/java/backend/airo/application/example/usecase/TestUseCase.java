package backend.airo.application.example.usecase;


import backend.airo.domain.example.Test;
import backend.airo.domain.example.command.CreateTestCommand;
import backend.airo.domain.example.exception.DomainErrorCode;
import backend.airo.domain.example.exception.TestException;
import backend.airo.domain.example.query.GetTestQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestUseCase {

    private final CreateTestCommand createTestCommand;
    private final GetTestQuery getTestQuery;

    public Test saveTest(String request) {
        if("CC".equals(request))
            throw TestException.notFound(DomainErrorCode.TEST_ERROR, "UseCase");

        return createTestCommand.handle(request);
    }

    public Test getTest(Long request) {
        return getTestQuery.handle(request);
    }
}
