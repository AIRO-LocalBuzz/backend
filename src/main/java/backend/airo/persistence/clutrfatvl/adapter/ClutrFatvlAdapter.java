package backend.airo.persistence.clutrfatvl.adapter;

import backend.airo.domain.clure_fatvl.ClutrFatvl;
import backend.airo.domain.clure_fatvl.repository.ClutrFatvlRepository;
import backend.airo.persistence.clutrfatvl.entity.ClutrFatvlEntity;
import backend.airo.persistence.clutrfatvl.repository.ClutrFatvlJpaRepository;
import backend.airo.persistence.example.entity.TestEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClutrFatvlAdapter implements ClutrFatvlRepository {

    private final ClutrFatvlJpaRepository clutrFatvlJpaRepository;

    @Override
    public List<ClutrFatvl> findAll() {
        List<ClutrFatvlEntity> clutrFatvlEntities = clutrFatvlJpaRepository.findAll();
        if (clutrFatvlEntities.isEmpty()) {
            return List.of();
        }
        return clutrFatvlEntities.stream().map(ClutrFatvlEntity::toDomain).toList();
    }

    @Override
    public Collection<ClutrFatvl> saveAll(Collection<ClutrFatvl> aggregates) {
        List<ClutrFatvlEntity> clutrFatvlEntities = aggregates.stream().map(ClutrFatvlEntity::toEntity).toList();
        List<ClutrFatvlEntity> saveClutrFatvlEntities = clutrFatvlJpaRepository.saveAll(clutrFatvlEntities);
        return saveClutrFatvlEntities.stream().map(ClutrFatvlEntity::toDomain).toList();
    }

    @Override
    public ClutrFatvl findById(Long aggregates) {
        ClutrFatvlEntity clutrFatvlEntity = clutrFatvlJpaRepository.findById(aggregates).orElseThrow(() ->
                new IllegalArgumentException("ClutrFatvl Not Found with id - " + aggregates)
        );
        return ClutrFatvlEntity.toDomain(clutrFatvlEntity);
    }

    @Override
    public ClutrFatvl save(ClutrFatvl aggregate) {
        return null;
    }
}
