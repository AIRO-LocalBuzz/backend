package backend.airo.domain.clure_fatvl.query;


import backend.airo.domain.clure_fatvl.ClutrFatvlInfo;
import backend.airo.domain.clure_fatvl.repository.ClturFatvlnfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetClutrFatvlInfoQuery {

    private final ClturFatvlnfoRepository clturFatvlnfoRepository;

    public ClutrFatvlInfo handle(Long contentId) {
        return clturFatvlnfoRepository.findById(contentId);
    }
}
