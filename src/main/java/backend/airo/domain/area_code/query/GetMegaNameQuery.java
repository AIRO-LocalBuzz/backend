package backend.airo.domain.area_code.query;

import backend.airo.domain.area_code.repository.MegaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetMegaNameQuery {

    private final MegaRepository megaRepository;

    public String handle(Long megaId) {
        return megaRepository.findByMegaCode(megaId);
    }

}
