package backend.airo.domain.rural_ex.query;

import backend.airo.domain.rural_ex.RuralEx;
import backend.airo.domain.rural_ex.repository.RuralExRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetRuralListQuery {

    private final RuralExRepository ruralExRepository;

    public Page<RuralEx> handle(String megaName, String cityName, Pageable pageable) {
        return ruralExRepository.findAll(megaName, cityName, pageable);
    }

}
