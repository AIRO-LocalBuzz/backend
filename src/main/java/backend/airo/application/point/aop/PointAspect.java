package backend.airo.application.point.aop;

import backend.airo.domain.point_history.command.CreateTradePointCommand;
import backend.airo.domain.point_history.event.PointAddedEvent;
import backend.airo.persistence.point_history.entity.PointHistoryEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PointAspect {

    private final ApplicationEventPublisher publisher;
    private final CreateTradePointCommand createTradePointCommand;

    //TODO 후기 save 이후로 pointcut 범위 조절
    @AfterReturning(
//            pointcut = "execution(* backend.airo.domain.point.repository.*Repository.save(..))"
    )
    public void afterSave(JoinPoint joinPoint) {
        Object arg = joinPoint.getArgs()[0];
        if (arg instanceof PointHistoryEntity inquiry) {
            PointAddedEvent event = new PointAddedEvent(
                    inquiry.getPoint(),
                    inquiry.getUserId(),
                    inquiry.getType()
            );
            publisher.publishEvent(event);
        }
    }
}
