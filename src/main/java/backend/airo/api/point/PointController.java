package backend.airo.api.point;

import backend.airo.api.annotation.UserPrincipal;
import backend.airo.api.global.dto.Response;
import backend.airo.api.point.dto.PointResponse;
import backend.airo.application.point.usecase.PointUseCase;
import backend.airo.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/point")
@RequiredArgsConstructor
@Slf4j
public class PointController {

    private final PointUseCase pointUseCase;

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public Response<PointResponse> getCountPoint(@UserPrincipal User user) {
        Long countPoint = pointUseCase.getCountPoint(user.getId());
        return Response.success(new PointResponse(countPoint));
    }

}
