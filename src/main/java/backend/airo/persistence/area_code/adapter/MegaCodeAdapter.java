package backend.airo.persistence.area_code.adapter;

import backend.airo.domain.area_code.MegaCode;
import backend.airo.domain.area_code.repository.MegaRepository;
import backend.airo.persistence.area_code.entity.MegaCodeEntity;
import backend.airo.persistence.area_code.repository.MegaCodeJpaRepository;
import backend.airo.persistence.shop.entity.ShopEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    public MegaCode findById(Long megaId) {
        return null;
    }


    @Override
    public List<MegaCode> findAll() {
        List<MegaCodeEntity> megaCodeEntities = megaCodeJpaRepository.findAll();
        return megaCodeEntities.stream().map(MegaCodeEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Long findByMegaName(String megaName) {
        return megaCodeJpaRepository.findByCtprvnNm(megaName).orElseThrow(() ->
                new IllegalArgumentException("Shop Not Found with Name - " + megaName)).getCtprvnCd();
    }

    @Override
    public String findByMegaCode(Long megaId) {
        return megaCodeJpaRepository.findById(megaId).orElseThrow(() ->
                new IllegalArgumentException("Shop Not Found with id - " + megaId)).getCtprvnNm();
    }
}
