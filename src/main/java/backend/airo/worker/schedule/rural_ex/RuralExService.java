package backend.airo.worker.schedule.rural_ex;

import backend.airo.domain.rural_ex.RuralEx;
import backend.airo.domain.rural_ex.command.CreateAllRuralExCommand;
import backend.airo.domain.rural_ex.port.RuralDataPort;
import backend.airo.support.time.TimeCatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RuralExService {

    private final RuralDataPort ruralDataPort;
    private final CreateAllRuralExCommand createAllRuralExCommand;
    private final TimeCatch timeCatch = new TimeCatch("RuralService Time Check");

    public void collectRuralExOf() {
        timeCatch.start();
        List<RuralEx> ruralExInfo = ruralDataPort.getRuralExInfo();
        createAllRuralExCommand.handle(ruralExInfo);
        timeCatch.end();
    }

}
