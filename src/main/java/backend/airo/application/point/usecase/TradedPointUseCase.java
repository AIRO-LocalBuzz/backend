package backend.airo.application.point.usecase;

import backend.airo.domain.point_history.TradePoint;
import backend.airo.domain.point_history.command.CreateTradePointCommand;
import backend.airo.domain.point_history.query.GetTradePointListQuery;
import backend.airo.domain.point_history.vo.TradePointStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TradedPointUseCase {

    private final CreateTradePointCommand createTradePointCommand;
    private final GetTradePointListQuery getTradePointListQuery;

    /**
     * 사용자의 구매 이력을 저장한다.
     * @param userId 포인트를 적립할 회원
     * @param point  구매 상품의 가격 ( 포인트 )
     * @param itemName  구매 상품 이름
     * @return 저장이 완료된 {@link TradePoint} 도메인 객체
     */
    public TradePoint saveTradePoint(Long userId, Long point, String itemName, TradePointStatus tradePointStatus) {
        return createTradePointCommand.handle(userId, point, itemName, tradePointStatus);
    }

    /**
     * 사용자의 구매 이력을 반환한다.
     * @param userId 구매이력을 조회할 회원
     * @return 저장이 완료된 {@link TradePoint} 도메인 객체 List
     */
    public List<TradePoint> getTradePointList(Long userId) {
        return getTradePointListQuery.handle(userId);
    }

}
