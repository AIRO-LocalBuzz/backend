package backend.airo.domain.area_code.command;

import backend.airo.domain.area_code.CityCode;
import backend.airo.domain.area_code.MegaCode;
import backend.airo.domain.area_code.repository.CityRepository;
import backend.airo.domain.area_code.repository.MegaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class CreateAllCityCodeCommand {

    private final CityRepository cityRepository;

    public void handle(List<CityCode> megaCodes) {
        cityRepository.saveAll(megaCodes);
    }

}
