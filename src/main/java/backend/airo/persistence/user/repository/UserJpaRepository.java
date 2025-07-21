package backend.airo.persistence.user.repository;

import backend.airo.persistence.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByFirebaseUid(String firebaseUid);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByFirebaseUid(String firebaseUid);
    boolean existsByEmail(String email);
}