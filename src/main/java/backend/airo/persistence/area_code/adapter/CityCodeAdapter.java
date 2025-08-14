package backend.airo.persistence.area_code.adapter;

import backend.airo.domain.area_code.CityCode;
import backend.airo.domain.area_code.repository.CityRepository;
import backend.airo.persistence.area_code.entity.CityCodeEntity;
import backend.airo.persistence.area_code.repository.CityCodeJpaRepository;
import backend.airo.persistence.point.entity.PointEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CityCodeAdapter implements CityRepository {

    private final CityCodeJpaRepository cityCodeJpaRepository;

    @Override
    public CityCode save(CityCode aggregate) {
        return null;
    }

    @Override
    public Collection<CityCode> saveAll(Collection<CityCode> aggregates) {
        List<CityCodeEntity> cityCodeEntities = aggregates.stream().map(CityCodeEntity::toEntity).toList();
        List<CityCodeEntity> saveCityCodeEntitys = cityCodeJpaRepository.saveAll(cityCodeEntities);
        return saveCityCodeEntitys.stream().map(CityCodeEntity::toDomain).toList();
    }

    @Override
    public CityCode findById(Long aLong) {
        return null;
    }

    @Override
    public List<CityCode> findAll() {
        List<CityCodeEntity> entities = cityCodeJpaRepository.findAll();
        return entities.stream()
                .map(CityCodeEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Long> findByCityCode(Long megaId, String cityName) {
        return cityCodeJpaRepository.findByCtprvnNmAndMegaCodeId(cityName, megaId)
                .map(CityCodeEntity::getCtprvnCd);
    }

    @Override
    public Optional<String> findByCityName(Long megaId, Long cityId) {
        return cityCodeJpaRepository.findByCtprvnCdAndMegaCodeId(cityId, megaId)
                .map(CityCodeEntity::getCtprvnNm);
    }
}
