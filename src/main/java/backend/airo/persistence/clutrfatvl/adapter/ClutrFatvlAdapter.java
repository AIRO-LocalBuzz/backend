package backend.airo.persistence.clutrfatvl.adapter;

import backend.airo.persistence.clutrfatvl.entity.ClutrFatvlEntity;
import backend.airo.persistence.clutrfatvl.repository.ClutrFatvlJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class ClutrFatvlAdapter {

    private final ClutrFatvlJpaRepository clutrFatvlJpaRepository;

    public void saveAll(List<ClutrFatvlEntity> clutrFatvlEntities) {
        clutrFatvlJpaRepository.saveAll(clutrFatvlEntities);
    }

}
