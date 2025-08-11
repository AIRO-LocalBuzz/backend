package backend.airo.domain.area_code.query;

import backend.airo.domain.area_code.repository.MegaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetMegaCodeQuery {

    private final MegaRepository megaRepository;

    public Long handle(String megaName) {
        return megaRepository.findByMegaName(megaName);
    }

}
