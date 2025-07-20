package backend.airo.persistence.example.adapter;

import backend.airo.domain.example.Test;
import backend.airo.domain.example.repository.TestRepository;
import backend.airo.persistence.example.entity.TestEntity;
import backend.airo.persistence.example.repository.TestJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class TestAdapter implements TestRepository {

    private final TestJpaRepository testJpaRepository;

    @Override
    public Test save(Test aggregate) {
        TestEntity entity = TestEntity.toEntity(aggregate);
        TestEntity saveTestEntity = testJpaRepository.save(entity);
        return TestEntity.toDomain(saveTestEntity);
    }

    @Override
    public Collection<Test> saveAll(Collection<Test> aggregates) {
        return List.of();
    }

    @Override
    public Test findById(Long aggregates) {
        TestEntity testEntity = testJpaRepository.findById(aggregates).orElseThrow(() ->
                new IllegalArgumentException("Test Not Found with id - " + aggregates)
        );
        return TestEntity.toDomain(testEntity);
    }
}
