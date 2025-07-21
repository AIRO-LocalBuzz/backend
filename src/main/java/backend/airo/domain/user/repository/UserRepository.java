package backend.airo.domain.user.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.user.User;

import java.util.Optional;

public interface UserRepository extends AggregateSupport<User, Long> {
    
    Optional<User> findByNickname(String nickname);
    
    boolean existsByNickname(String nickname);
}