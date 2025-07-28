package backend.airo.persistence.area_code.adapter;

import backend.airo.domain.area_code.MegaCode;
import backend.airo.domain.area_code.repository.MegaRepository;
import backend.airo.persistence.area_code.entity.MegaCodeEntity;
import backend.airo.persistence.area_code.repository.MegaCodeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MegaCodeAdapter implements MegaRepository {

    private final MegaCodeJpaRepository megaCodeJpaRepository;

    @Override
    public MegaCode save(MegaCode aggregate) {
        return null;
    }

    @Override
    public Collection<MegaCode> saveAll(Collection<MegaCode> aggregates) {
        List<MegaCodeEntity> megaCodeEntities = aggregates.stream().map(MegaCodeEntity::toEntity).toList();
        List<MegaCodeEntity> saveMegaCodeEntities = megaCodeJpaRepository.saveAll(megaCodeEntities);
        return saveMegaCodeEntities.stream().map(MegaCodeEntity::toDomain).toList();
    }

    @Override
    public MegaCode findById(Long aLong) {
        return null;
    }

    @Override
    public List<MegaCode> findAll() {
        List<MegaCodeEntity> megaCodeEntities = megaCodeJpaRepository.findAll();
        return megaCodeEntities.stream().map(MegaCodeEntity::toDomain).toList();
    }

    @Override
    public MegaCode findByCtprvnCode(Long ctprvnCode) {
        MegaCodeEntity megaCodeEntity = megaCodeJpaRepository.findByCtprvnCd(ctprvnCode);
        return MegaCodeEntity.toDomain(megaCodeEntity);
    }
}
