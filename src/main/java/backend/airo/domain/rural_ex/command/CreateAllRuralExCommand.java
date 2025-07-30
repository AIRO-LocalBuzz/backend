package backend.airo.domain.rural_ex.command;

import backend.airo.domain.rural_ex.RuralEx;
import backend.airo.domain.rural_ex.repository.RuralExRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateAllRuralExCommand {

    private final RuralExRepository ruralExRepository;

    public void handle(List<RuralEx> ruralExes) {
        ruralExRepository.saveAll(ruralExes);
    }


}
