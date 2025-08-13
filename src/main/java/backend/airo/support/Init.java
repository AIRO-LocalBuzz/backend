package backend.airo.support;

import backend.airo.cache.area_code.AreaCodeCacheService;
import backend.airo.domain.area_code.MegaCode;
import backend.airo.domain.area_code.repository.MegaRepository;
import backend.airo.worker.schedule.area_code.AreaCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Init {

    private final AreaCodeService areaCodeService;
    private final AreaCodeCacheService areaCodeCacheService;

    private final MegaRepository megaRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initAreaCode() {
        List<MegaCode> megaCodes = megaRepository.findAll();
        if (megaCodes.isEmpty()) {
            areaCodeService.collectCodeOf();
        }
        try {
            areaCodeCacheService.getCityAllList();
            areaCodeCacheService.getMegaAllList();
            log.info("Redis cache hit");
        } catch (Exception e) {
            log.error("Redis cache miss :: {}", e.getMessage());
        }
    }
}
