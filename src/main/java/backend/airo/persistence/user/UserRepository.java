package backend.airo.persistence.user;

import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndProviderType(String email, ProviderType providerType);
    Optional<User> findByProviderIdAndProviderType(String providerId, ProviderType providerType);
}