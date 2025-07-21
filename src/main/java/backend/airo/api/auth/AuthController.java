package backend.airo.api.auth;

import backend.airo.api.global.dto.Response;
import backend.airo.application.auth.usecase.LogoutUseCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LogoutUseCase logoutUseCase;

    @PostMapping("/logout")
    public ResponseEntity<Response<Void>> logout(HttpServletRequest request) {
        logoutUseCase.logout(request);
        return ResponseEntity.ok(Response.success("로그아웃 되었습니다."));
    }
}