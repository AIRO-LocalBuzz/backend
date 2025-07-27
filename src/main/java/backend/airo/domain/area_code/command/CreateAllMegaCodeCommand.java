package backend.airo.domain.area_code.command;

import backend.airo.domain.area_code.MegaCode;
import backend.airo.domain.area_code.repository.MegaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;


@Component
@RequiredArgsConstructor
public class CreateAllMegaCodeCommand {

    private final MegaRepository megaRepository;

    public List<MegaCode> handle(List<MegaCode> megaCodes) {
        Collection<MegaCode> megaCodes1 = megaRepository.saveAll(megaCodes);
        return (List<MegaCode>) megaCodes1;
    }
}
