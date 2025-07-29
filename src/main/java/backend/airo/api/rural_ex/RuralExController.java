package backend.airo.api.rural_ex;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "RuralEx", description = "전국 농어촌 체험 휴향 마을 관련 API V1")
public class RuralExController {



}
