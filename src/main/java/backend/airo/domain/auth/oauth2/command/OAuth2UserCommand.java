package backend.airo.domain.auth.oauth2.command;

import backend.airo.domain.user.User;

public interface OAuth2UserCommand {
    User save(User user);
}
