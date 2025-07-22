package backend.airo.application.point.usecase;

import backend.airo.domain.example.exception.DomainErrorCode;
import backend.airo.domain.point.PointHistory;
import backend.airo.domain.point.TradePoint;
import backend.airo.domain.point.command.CreatePointCommand;
import backend.airo.domain.point.event.TradePointAddedEvent;
import backend.airo.domain.point.exception.PointException;
import backend.airo.domain.point.query.GetPointListQuery;
import backend.airo.domain.point.query.GetPointScoreQuery;
import backend.airo.domain.point.query.GetTradePointScoreQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PointHistoryUseCase {

    private final ApplicationEventPublisher publisher;

    private final CreatePointCommand createPointCommand;
    private final GetPointListQuery getPointListQuery;
    private final GetPointScoreQuery getPointScoreQuery;
    private final GetTradePointScoreQuery tradePointScoreQuery;

    /**
     * 사용자의 후기 작성에 따라 포인트를 적립한다.
     * @param userId 포인트를 적립할 회원
     * @param point  적립할 포인트 금액
     * @return 저장이 완료된 {@link PointHistory} 도메인 객체
     */
    public PointHistory savePoint(Long userId, Long point) {
        return createPointCommand.handle(userId, point);
    }

    /**
     * 사용자의 포인트 적립 내역을 반환한다.
     * @param userId 포인트 적립 내역을 확인할 회원
     * @return 저장이 완료된 {@link PointHistory} 도메인 객체 List
     */
    public List<PointHistory> getPointList(Long userId) {
        return getPointListQuery.handle(userId);
    }

    /**
     * 사용자의 현재 포인트에서 특정 포인트를 차감한다. [ 상품 구매 ]
     * @param userId 포인트를 차감 할 회원
     * @return 포인트 차감 여부
     */
    public void usePoints(Long userId, Long itemPoint, String itemName) {
        Long tradeScore = tradePointScoreQuery.handle(userId);
        Long pointScore = getPointScoreQuery.handle(userId);

        //해당 잔액을 보관하거나 Redis에 기록하거나 어떻게든 기록을 해야 함.
        long usePoint = (pointScore - tradeScore) - itemPoint;

        TradePoint tradePoint = new TradePoint(
                0L,
                itemPoint,
                userId,
                itemName,
                null,
                null
        );

        try{
            if (usePoint < 0) {
                tradePoint.markFailure();
                throw PointException.notEnoughPoint(DomainErrorCode.NOT_ENOUGH_POINT, "PointUseCase");
            }
            tradePoint.markSuccess();
        }finally {
            publisher.publishEvent(new TradePointAddedEvent(tradePoint));
        }
    }

}
