package backend.airo.domain.clure_fatvl.query;

import backend.airo.domain.clure_fatvl.ClutrFatvl;
import backend.airo.domain.clure_fatvl.repository.ClutrFatvlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetClutrFatvlQuery {

    private final ClutrFatvlRepository clutrFatvlRepository;

    public ClutrFatvl handle(Long clureFatvl) {
        return clutrFatvlRepository.findById(
                clureFatvl
        );
    }

}
