package backend.airo.persistence.user.adapter;

import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;
import backend.airo.domain.user.repository.UserRepository;
import backend.airo.persistence.post.repository.PostLikeJpaRepository;
import backend.airo.persistence.user.entity.UserEntity;
import backend.airo.persistence.user.repository.UserJpaRepository;
import backend.airo.persistence.post.repository.PostJpaRepository;

import backend.airo.persistence.comment.repository.CommentJpaRepository;
import backend.airo.persistence.image.repository.ImageJpaRepository;
import backend.airo.persistence.point.repository.PointJpaRepository;
import backend.airo.persistence.point_history.repository.PointHistoryJpaRepository;
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
    
    // 연관 엔티티들의 JpaRepository 주입
    private final PostJpaRepository postJpaRepository;
    private final PostLikeJpaRepository postLikeJpaRepository;
    private final CommentJpaRepository commentJpaRepository;
    private final ImageJpaRepository imageJpaRepository;
    private final PointJpaRepository pointJpaRepository;
    private final PointHistoryJpaRepository pointHistoryJpaRepository;

    @Override
    public User save(User aggregate) {
        UserEntity productEntity = userJpaRepository.findById(aggregate.getId())
                .map(getProductEntity -> {
                    getProductEntity.updateUserInfo(aggregate.getName(), aggregate.getNickname(), aggregate.getPhoneNumber(), aggregate.getBirthDate());
                    return getProductEntity;
                }).orElseGet(() -> UserEntity.toEntity(aggregate));
        UserEntity saveProductEntity = userJpaRepository.save(productEntity);
        return UserEntity.toDomain(saveProductEntity);
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

    @Override
    public void deleteUserWithRelatedData(Long userId) {
        // 1. 연관된 이미지 삭제
        imageJpaRepository.deleteByUserId(userId);
        
        // 2. 연관된 댓글 삭제
        commentJpaRepository.deleteByUserId(userId);
        
        // 3. 연관된 게시글 좋아요 삭제
        postLikeJpaRepository.deleteByUserId(userId);
        
        // 4. 연관된 포인트 히스토리 삭제
        pointHistoryJpaRepository.deleteByUserId(userId);
        
        // 5. 연관된 포인트 삭제
        pointJpaRepository.deleteByUserId(userId);
        
        // 6. 연관된 게시글 삭제
        postJpaRepository.deleteByUserId(userId);
        
        // 7. 마지막으로 User 삭제
        userJpaRepository.deleteById(userId);
    }
}
