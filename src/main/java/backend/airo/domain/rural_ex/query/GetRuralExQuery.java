package backend.airo.domain.rural_ex.query;

import backend.airo.domain.rural_ex.RuralEx;
import backend.airo.domain.rural_ex.repository.RuralExRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetRuralExQuery {

    private final RuralExRepository ruralExRepository;

    public RuralEx handle(Long ruralExId) {
        return ruralExRepository.findById(
                ruralExId
        );
    }

}
