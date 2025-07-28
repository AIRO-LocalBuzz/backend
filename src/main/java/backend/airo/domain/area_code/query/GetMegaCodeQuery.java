package backend.airo.domain.area_code.query;

import backend.airo.domain.area_code.MegaCode;
import backend.airo.domain.area_code.repository.MegaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetMegaCodeQuery {

    private final MegaRepository megaRepository;

    public MegaCode handle(Long ctprvnCode) {
        return megaRepository.findByCtprvnCode(ctprvnCode);
    }
}
