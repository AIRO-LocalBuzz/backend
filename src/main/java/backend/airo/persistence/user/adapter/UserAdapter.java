package backend.airo.persistence.user.adapter;

import backend.airo.domain.user.User;
import backend.airo.domain.user.repository.UserRepository;
import backend.airo.domain.user.enums.ProviderType;
import backend.airo.persistence.user.entity.UserEntity;
import backend.airo.persistence.user.repository.UserJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository
@Transactional
@RequiredArgsConstructor
public class UserAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User aggregate) {
        UserEntity entity = UserEntity.toEntity(aggregate);
        UserEntity savedEntity = userJpaRepository.save(entity);
        return UserEntity.toDomain(savedEntity);
    }

    @Override
    public Collection<User> saveAll(Collection<User> aggregates) {
        List<UserEntity> entities = aggregates.stream()
                .map(UserEntity::toEntity)
                .toList();

        List<UserEntity> savedEntities = userJpaRepository.saveAll(entities);

        return savedEntities.stream()
                .map(UserEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<User> findByProviderIdAndProviderType(String providerId, ProviderType providerType) {
        return userJpaRepository.findByProviderIdAndProviderType(providerId, providerType)
                .map(UserEntity::toDomain);
    }

    @Override
    public User findById(Long id) {
        UserEntity userEntity = userJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User Not Found with id - " + id));
        return UserEntity.toDomain(userEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(UserEntity::toDomain);

    }

}
