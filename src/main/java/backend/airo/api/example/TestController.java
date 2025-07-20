package backend.airo.api.example;

import backend.airo.api.global.dto.Response;
import backend.airo.api.example.dto.TestResponse;
import backend.airo.application.example.usecase.TestUseCase;
import backend.airo.domain.example.Test;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class TestController {

    private final TestUseCase testUseCase;

    @PostMapping("/test/controller")
    public Response<TestResponse> testSaveController(@RequestParam String request) {
        Test test = testUseCase.saveTest(request);
        return Response.success(new TestResponse(test.getId(), test.getTest()));
    }

    @GetMapping("/test/controller")
    public Response<TestResponse> testGetController(@RequestParam Long request) {
        Test test = testUseCase.getTest(request);
        return Response.success(new TestResponse(test.getId(), test.getTest()));
    }
}
