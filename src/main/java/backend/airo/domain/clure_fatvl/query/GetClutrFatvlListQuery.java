package backend.airo.domain.clure_fatvl.query;

import backend.airo.domain.clure_fatvl.ClutrFatvl;
import backend.airo.domain.clure_fatvl.repository.ClutrFatvlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetClutrFatvlListQuery {

    private final ClutrFatvlRepository clutrFatvlRepository;

    public Page<ClutrFatvl> handle(Integer megaName, Integer cityName, Pageable pageable) {
        return clutrFatvlRepository.findAll(megaName, cityName, pageable);
    }

}
