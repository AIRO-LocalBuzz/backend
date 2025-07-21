package backend.airo.persistence.user.adapter;

import backend.airo.domain.user.UserProfile;
import backend.airo.domain.user.repository.UserProfileRepository;
import backend.airo.persistence.user.entity.UserProfileEntity;
import backend.airo.persistence.user.repository.UserProfileJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserProfileAdapter implements UserProfileRepository {

    private final UserProfileJpaRepository userProfileJpaRepository;

    @Override
    public UserProfile save(UserProfile userProfile) {
        UserProfileEntity entity = UserProfileEntity.toEntity(userProfile);
        if (userProfile.getId() != 0) {
            entity = userProfileJpaRepository.findById(userProfile.getId())
                    .map(existingEntity -> existingEntity.update(
                            userProfile.getName(),
                            userProfile.getContact(),
                            userProfile.getBirthDate(),
                            userProfile.getEmail()
                    ))
                    .orElse(entity);
        }
        UserProfileEntity savedEntity = userProfileJpaRepository.save(entity);
        return UserProfileEntity.toDomain(savedEntity);
    }

    @Override
    public Optional<UserProfile> findById(Long id) {
        return userProfileJpaRepository.findById(id)
                .map(UserProfileEntity::toDomain);
    }

    @Override
    public List<UserProfile> findAll() {
        return userProfileJpaRepository.findAll()
                .stream()
                .map(UserProfileEntity::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        userProfileJpaRepository.deleteById(id);
    }
}