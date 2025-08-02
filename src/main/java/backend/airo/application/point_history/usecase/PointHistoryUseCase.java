package backend.airo.application.point_history.usecase;


import backend.airo.domain.point_history.PointHistory;
import backend.airo.domain.point_history.query.GetPointHistoryListQuery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PointHistoryUseCase {

    private final GetPointHistoryListQuery getPointHistoryListQuery;

    /**
     * 사용자의 포인트 적립 내역을 반환한다.
     * @param userId 포인트 적립 내역을 확인할 회원
     * @return 저장이 완료된 {@link PointHistory} 도메인 객체 List
     */
    public List<PointHistory> getPointHistoryList(Long userId) {
        return getPointHistoryListQuery.handle(userId);
    }

//    /**
//     * 사용자의 현재 포인트에서 특정 포인트를 차감한다. [ 상품 구매 ]  [ 이벤트 ]
//     * @param userId 포인트를 차감 할 회원
//     * @return 포인트 차감 여부
//     */
//    public Long usePoints(Long userId, Long itemPoint, String itemName) {
////        Long tradeScore = tradePointScoreQuery.handle(userId);
////        Long pointScore = getPointScoreQuery.handle(userId);
//        Long point = getPointQuery.handle(userId);
//
//        long resultPoint = point - itemPoint;
//
//        TradePoint tradePoint = TradePoint.create(itemPoint,userId,itemName);
//
//            try{
//                if (resultPoint < 0) {
//                    tradePoint.markFailure(TradePointStatus.NOT_ENOUGH_POINT);
//                    throw PointHistoryException.notEnoughPoint(DomainErrorCode.NOT_ENOUGH_POINT, "PointHistoryUseCase");
//                }
//                updatePointCommand.handle(userId, resultPoint);
//            tradePoint.markSuccess();
//        }finally {
//            publisher.publishEvent(new TradePointAddedEvent(tradePoint));
//        }
//        return resultPoint;
//    }
}
