package backend.airo.application.point.usecase;

import backend.airo.domain.example.exception.DomainErrorCode;

import backend.airo.domain.point.command.UpdatePointCommand;
import backend.airo.domain.point.query.GetPointQuery;

import backend.airo.domain.point_history.PointHistory;
import backend.airo.domain.point_history.TradePoint;
import backend.airo.domain.point_history.command.CreatePointHistoryCommand;
import backend.airo.domain.point_history.event.TradePointAddedEvent;
import backend.airo.domain.point_history.exception.PointHistoryException;
import backend.airo.domain.point_history.query.GetPointHistoryListQuery;
import backend.airo.domain.point_history.query.GetPointScoreQuery;
import backend.airo.domain.point_history.query.GetTradePointScoreQuery;
import backend.airo.domain.point_history.vo.TradePointStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PointHistoryUseCase {

    private final ApplicationEventPublisher publisher;

    private final CreatePointHistoryCommand createPointHistoryCommand;
    private final GetPointHistoryListQuery getPointHistoryListQuery;
    private final GetPointScoreQuery getPointScoreQuery;
    private final GetTradePointScoreQuery tradePointScoreQuery;
    private final GetPointQuery getPointQuery;

    //TODO 후기 작성 트랜잭션 후로 아래의 메서드 옮기기
    private final UpdatePointCommand updatePointCommand;

    /**
     * 사용자의 적립 내역을 저장한다. [ 이벤트 ]
     * @param userId 포인트 기록 내역 회원
     * @param point  기록할 포인트 금액
     * @return 저장이 완료된 {@link PointHistory} 도메인 객체
     */
    public void savePointHistory(Long userId, Long point) {
        //TODO 후기 작성 트랜잭션 후로 아래의 메서드 옮기기
//        updatePointCommand.handle(userId, point);
        createPointHistoryCommand.handle(userId, point);
    }

    /**
     * 사용자의 포인트 적립 내역을 반환한다.
     * @param userId 포인트 적립 내역을 확인할 회원
     * @return 저장이 완료된 {@link PointHistory} 도메인 객체 List
     */
    public List<PointHistory> getPointList(Long userId) {
        return getPointHistoryListQuery.handle(userId);
    }

    /**
     * 사용자의 현재 포인트에서 특정 포인트를 차감한다. [ 상품 구매 ]  [ 이벤트 ]
     * @param userId 포인트를 차감 할 회원
     * @return 포인트 차감 여부
     */
    public Long usePoints(Long userId, Long itemPoint, String itemName) {
//        Long tradeScore = tradePointScoreQuery.handle(userId);
//        Long pointScore = getPointScoreQuery.handle(userId);
        Long point = getPointQuery.handle(userId);

        long resultPoint = point - itemPoint;

        TradePoint tradePoint = TradePoint.create(itemPoint,userId,itemName);

            try{
                if (resultPoint < 0) {
                    tradePoint.markFailure(TradePointStatus.NOT_ENOUGH_POINT);
                    throw PointHistoryException.notEnoughPoint(DomainErrorCode.NOT_ENOUGH_POINT, "PointHistoryUseCase");
                }
                updatePointCommand.handle(userId, resultPoint);
            tradePoint.markSuccess();
        }finally {
            publisher.publishEvent(new TradePointAddedEvent(tradePoint));
        }
        return resultPoint;
    }
}
