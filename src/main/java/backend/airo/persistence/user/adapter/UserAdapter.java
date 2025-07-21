package backend.airo.persistence.user.adapter;

import backend.airo.domain.user.User;
import backend.airo.domain.user.repository.UserRepository;
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
        UserEntity entity;
        if (aggregate.getId() != null) {
            // Update existing user
            entity = userJpaRepository.findById(aggregate.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id - " + aggregate.getId()));
            entity.updateNickname(aggregate.getNickname());
        } else {
            // Create new user
            entity = UserEntity.toEntity(aggregate);
        }
        
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
    public User findById(Long id) {
        UserEntity entity = userJpaRepository.findById(id)
            .orElse(null);
        return entity != null ? UserEntity.toDomain(entity) : null;
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        return userJpaRepository.findByNickname(nickname)
            .map(UserEntity::toDomain);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return userJpaRepository.existsByNickname(nickname);
    }
}