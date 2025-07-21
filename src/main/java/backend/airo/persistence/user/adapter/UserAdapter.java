package backend.airo.persistence.user.adapter;

import backend.airo.domain.user.User;
import backend.airo.domain.user.repository.UserRepository;
import backend.airo.persistence.user.entity.UserEntity;
import backend.airo.persistence.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        UserEntity userEntity = UserEntity.fromDomain(user);
        UserEntity savedEntity = userJpaRepository.save(userEntity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id)
                .map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByFirebaseUid(String firebaseUid) {
        return userJpaRepository.findByFirebaseUid(firebaseUid)
                .map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(UserEntity::toDomain);
    }

    @Override
    public boolean existsByFirebaseUid(String firebaseUid) {
        return userJpaRepository.existsByFirebaseUid(firebaseUid);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
}