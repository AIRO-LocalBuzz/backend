package backend.airo.persistence.category.repository;

import backend.airo.persistence.category.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {
}
