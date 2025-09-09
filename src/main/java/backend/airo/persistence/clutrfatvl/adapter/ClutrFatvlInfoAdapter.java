package backend.airo.persistence.clutrfatvl.adapter;

import backend.airo.domain.clure_fatvl.ClutrFatvlInfo;
import backend.airo.domain.clure_fatvl.repository.ClturFatvlnfoRepository;
import backend.airo.persistence.clutrfatvl.entity.ClutrFatvlEntity;
import backend.airo.persistence.clutrfatvl.entity.ClutrFatvlInfoEntity;
import backend.airo.persistence.clutrfatvl.repository.ClutrFatvlInfoJpaRepository;
import backend.airo.persistence.clutrfatvl.repository.ClutrFatvlJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClutrFatvlInfoAdapter implements ClturFatvlnfoRepository {

    private final ClutrFatvlInfoJpaRepository clutrFatvlInfoJpaRepository;

    @Override
    public ClutrFatvlInfo save(ClutrFatvlInfo aggregate) {
        return null;
    }

    @Override
    public Collection<ClutrFatvlInfo> saveAll(Collection<ClutrFatvlInfo> aggregates) {
        return List.of();
    }

    @Override
    public ClutrFatvlInfo findById(Long contentId) {
        ClutrFatvlInfoEntity clutrFatvlInfoEntity = clutrFatvlInfoJpaRepository.findById(contentId).orElseThrow(() ->
                new IllegalArgumentException("ClutrFatvlInfo Not Found with id - " + contentId)
        );
        return ClutrFatvlInfoEntity.toDomain(clutrFatvlInfoEntity);
    }
}
