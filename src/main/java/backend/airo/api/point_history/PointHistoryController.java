package backend.airo.api.point_history;

import backend.airo.api.annotation.UserPrincipal;
import backend.airo.api.global.dto.Response;
import backend.airo.api.point_history.dto.PointHistoryResponse;
import backend.airo.application.point_history.usecase.PointHistoryUseCase;
import backend.airo.domain.point_history.PointHistory;
import backend.airo.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/point/history")
@RequiredArgsConstructor
@Slf4j
public class PointHistoryController {

    private final PointHistoryUseCase pointHistoryUseCase;

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public Response<List<PointHistoryResponse>> getPointHistory(@UserPrincipal User user) {
        List<PointHistory> pointHistoryList = pointHistoryUseCase.getPointHistoryList(user.getId());
        return Response.success(pointHistoryList.stream().map(list ->
                new PointHistoryResponse(
                        list.getPoint(),
                        list.getType(),
                        list.getPostId(),
                        list.getCreateAt()
                )).toList()
        );
    }

}
