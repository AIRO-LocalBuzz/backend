package backend.airo.persistence.area_code.adapter;

import backend.airo.domain.area_code.CityCode;
import backend.airo.domain.area_code.repository.CityRepository;
import backend.airo.persistence.area_code.entity.CityCodeEntity;
import backend.airo.persistence.area_code.repository.CityCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CityCodeAdapter implements CityRepository {

    private final CityCodeRepository cityCodeRepository;

    @Override
    public CityCode save(CityCode aggregate) {
        return null;
    }

    @Override
    public Collection<CityCode> saveAll(Collection<CityCode> aggregates) {
        List<CityCodeEntity> cityCodeEntities = aggregates.stream().map(CityCodeEntity::toEntity).toList();
        List<CityCodeEntity> saveCityCodeEntitys = cityCodeRepository.saveAll(cityCodeEntities);
        return saveCityCodeEntitys.stream().map(CityCodeEntity::toDomain).toList();
    }

    @Override
    public CityCode findById(Long aLong) {
        return null;
    }

    @Override
    public List<CityCode> findAll() {
        List<CityCodeEntity> cityCodeEntities = cityCodeRepository.findAll();
        return cityCodeEntities.stream().map(CityCodeEntity::toDomain).toList();
    }
}
