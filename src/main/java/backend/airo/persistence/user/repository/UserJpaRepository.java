package backend.airo.persistence.user.repository;

import backend.airo.persistence.user.entity.ProviderType;
import backend.airo.persistence.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByProviderIdAndProviderType(String providerId, ProviderType providerType);
    Optional<UserEntity> findById(Long id);

}