package backend.airo.domain.area_code.command;

import backend.airo.domain.area_code.MegaCode;
import backend.airo.domain.area_code.repository.MegaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class CreateAllMegaCodeCommand {

    private final MegaRepository megaRepository;

    public void handle(List<MegaCode> megaCodes) {
        megaRepository.saveAll(megaCodes);
    }

}
