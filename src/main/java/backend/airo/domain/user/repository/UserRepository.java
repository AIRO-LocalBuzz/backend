package backend.airo.domain.user.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;

import java.util.Optional;

public interface UserRepository extends AggregateSupport<User, Long> {
    Optional<User> findByProviderIdAndProviderType(String providerId, ProviderType providerType);

    User findById(Long id);

    Optional<User> findByEmail(String email);

    // User와 연관된 모든 데이터를 함께 삭제
    void deleteUserWithRelatedData(Long userId);
}
