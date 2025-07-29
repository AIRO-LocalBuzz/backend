package backend.airo.persistence.rural_ex.adapter;

import backend.airo.domain.rural_ex.RuralEx;
import backend.airo.domain.rural_ex.repository.RuralExRepository;
import backend.airo.persistence.rural_ex.entity.RuralExEntity;
import backend.airo.persistence.rural_ex.repository.RuralExJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RuralExAdapter implements RuralExRepository {

    private final RuralExJpaRepository ruralExJpaRepository;

    @Override
    public RuralEx save(RuralEx aggregate) {
        return null;
    }

    @Override
    public Collection<RuralEx> saveAll(Collection<RuralEx> aggregates) {
        List<RuralExEntity> ruralExEntities = aggregates.stream().map(RuralExEntity::toEntity).toList();
        List<RuralExEntity> saveruralExEntities = ruralExJpaRepository.saveAll(ruralExEntities);
        return saveruralExEntities.stream().map(RuralExEntity::toDomain).toList();
    }

    @Override
    public RuralEx findById(Long aLong) {
        return null;
    }
}
