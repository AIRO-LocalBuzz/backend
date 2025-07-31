package backend.airo.persistence.rural_ex.adapter;

import backend.airo.domain.rural_ex.RuralEx;
import backend.airo.domain.rural_ex.repository.RuralExRepository;
import backend.airo.persistence.rural_ex.entity.RuralExEntity;
import backend.airo.persistence.rural_ex.repository.RuralExJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RuralExAdapter implements RuralExRepository {

    private final RuralExJpaRepository ruralExJpaRepository;

    @Override
    public Page<RuralEx> findAll(String megaName, String cityName, Pageable pageable) {
        Page<RuralExEntity> ruralExEntities = ruralExJpaRepository.findByCtprvnNmAndSignguNm(megaName, cityName, pageable);
        if (ruralExEntities.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }
        return ruralExEntities.map(RuralExEntity::toDomain);
    }

    @Override
    public Collection<RuralEx> saveAll(Collection<RuralEx> aggregates) {
        List<RuralExEntity> ruralExEntities = aggregates.stream().map(RuralExEntity::toEntity).toList();
        List<RuralExEntity> saveruralExEntities = ruralExJpaRepository.saveAll(ruralExEntities);
        return saveruralExEntities.stream().map(RuralExEntity::toDomain).toList();
    }

    @Override
    public RuralEx findById(Long ruralExId) {
        RuralExEntity ruralExEntity = ruralExJpaRepository.findById(ruralExId).orElseThrow(() ->
                new IllegalArgumentException("RuralEx Not Found with id - " + ruralExId)
        );
        return RuralExEntity.toDomain(ruralExEntity);
    }

    @Override
    public RuralEx save(RuralEx aggregate) {
        return null;
    }

}
