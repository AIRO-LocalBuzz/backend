package backend.airo.api.content;

import backend.airo.api.example.dto.TestResponse;
import backend.airo.api.global.dto.Response;
import backend.airo.application.content.usecase.ContentUseCase;
import backend.airo.domain.content.vo.Region;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "content", description = "콘텐츠 관련 API")
public class ContentController {

    private final ContentUseCase contentUseCase;

    @GetMapping("/content")
    public Response<Void> findContent(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "SEOUL") Region region,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
//        Test test = testUseCase.saveTest(request);
        return Response.success();
    }

}
