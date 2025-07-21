package backend.airo.domain.user.repository;

import backend.airo.domain.user.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByFirebaseUid(String firebaseUid);
    Optional<User> findByEmail(String email);
    boolean existsByFirebaseUid(String firebaseUid);
    boolean existsByEmail(String email);
}