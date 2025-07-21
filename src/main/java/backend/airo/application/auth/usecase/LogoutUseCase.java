package backend.airo.application.auth.usecase;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogoutUseCase {

    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            log.info("로그아웃 - 세션 무효화: {}", session.getId());
            session.invalidate();
        } else {
            log.info("로그아웃 요청 - 세션이 존재하지 않음");
        }
    }
}