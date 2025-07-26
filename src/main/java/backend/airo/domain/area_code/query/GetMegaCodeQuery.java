package backend.airo.domain.area_code.query;

import backend.airo.domain.area_code.MegaCode;
import backend.airo.domain.area_code.repository.MegaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetMegaCodeQuery {

    private final MegaRepository megaRepository;

    public List<MegaCode> handle() {
        return megaRepository.findAll();
    }

}
