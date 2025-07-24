package backend.airo.domain.clure_fatvl.query;

import backend.airo.domain.clure_fatvl.ClutrFatvl;
import backend.airo.domain.clure_fatvl.repository.ClutrFatvlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetClutrFatvlListQuery {

    private final ClutrFatvlRepository clutrFatvlRepository;

    public List<ClutrFatvl> handle() {
        return clutrFatvlRepository.findAll();
    }

}
