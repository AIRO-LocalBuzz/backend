package backend.airo.domain.area_code.command;

import backend.airo.domain.clure_fatvl.repository.ClutrFatvlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DeleteAllClutrFatvlByDateCommand {

    private final ClutrFatvlRepository clutrFatvlRepository;

    public void handle(LocalDate start, LocalDate end) {
        clutrFatvlRepository.deleteAllByDate(start, end);
    }

}
