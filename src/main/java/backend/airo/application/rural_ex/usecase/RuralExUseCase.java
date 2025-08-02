package backend.airo.application.rural_ex.usecase;

import backend.airo.domain.rural_ex.RuralEx;
import backend.airo.domain.rural_ex.query.GetRuralExQuery;
import backend.airo.domain.rural_ex.query.GetRuralListQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RuralExUseCase {

    private final GetRuralListQuery getRuralListQuery;
    private final GetRuralExQuery getRuralExQuery;

    public Page<RuralEx> getRuralListQuery(String megaName, String cityName, Pageable pageable) {
        return getRuralListQuery.handle(megaName, cityName, pageable);
    }

    public RuralEx getClutrFatvlInfo(Long ruralExId) {
        return getRuralExQuery.handle(ruralExId);
    }
}
