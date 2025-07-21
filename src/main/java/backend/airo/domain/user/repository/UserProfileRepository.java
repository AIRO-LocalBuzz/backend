package backend.airo.domain.user.repository;

import backend.airo.domain.user.UserProfile;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository {
    UserProfile save(UserProfile userProfile);
    Optional<UserProfile> findById(Long id);
    List<UserProfile> findAll();
    void deleteById(Long id);
}