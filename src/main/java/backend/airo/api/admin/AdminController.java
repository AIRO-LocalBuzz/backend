package backend.airo.api.admin;

import backend.airo.application.admin.usecase.AdminUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin 관련 API V1")
public class AdminController {

    private final AdminUseCase adminUseCase;



}
