package backend.airo.domain.clure_fatvl.command;

import backend.airo.domain.clure_fatvl.ClutrFatvl;
import backend.airo.domain.clure_fatvl.repository.ClutrFatvlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateAllClutrFatvlCommand {

    private final ClutrFatvlRepository clutrFatvlRepository;

    public int handle(List<ClutrFatvl> allEntities) {
        return clutrFatvlRepository.saveAll(allEntities).size();
    }

}
