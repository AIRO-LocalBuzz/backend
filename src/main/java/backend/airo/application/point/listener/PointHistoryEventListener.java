package backend.airo.application.point.listener;

import backend.airo.application.point.usecase.PointHistoryUseCase;
import backend.airo.domain.point_history.event.PointHistoryAddedEvent;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointHistoryEventListener {

    private final PointHistoryUseCase pointHistoryUseCase;


    @Description("후기 작성시 사용자에게 포인트를 적립한다.")
    @Async
    @EventListener(PointHistoryAddedEvent.class)
    public void onInquiryAddedEvent(PointHistoryAddedEvent event) {
        pointHistoryUseCase.savePointHistory(
                event.getUserId(),
                event.getPoint()
        );
    }

}
