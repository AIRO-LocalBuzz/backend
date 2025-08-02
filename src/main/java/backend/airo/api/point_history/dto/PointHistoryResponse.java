package backend.airo.api.point_history.dto;

import backend.airo.domain.point_history.vo.PointType;

import java.time.LocalDateTime;

public record PointHistoryResponse(

        Long point,
        PointType type,
        Long postId,
        LocalDateTime createAt

) {
}
