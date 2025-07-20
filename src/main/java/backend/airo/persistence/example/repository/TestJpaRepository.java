package backend.airo.persistence.example.repository;

import backend.airo.persistence.example.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestJpaRepository extends JpaRepository<TestEntity, Long> {
}
