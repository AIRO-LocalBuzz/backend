package backend.airo.application.auth.usecase;

import backend.airo.api.auth.dto.AuthResponse;
import backend.airo.api.auth.dto.SignInRequest;
import backend.airo.domain.auth.command.GenerateJwtTokenCommand;
import backend.airo.domain.auth.oauth2.query.GetUserByTempCodeQuery;
import backend.airo.domain.user.User;
import backend.airo.domain.user.command.CreateUserCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SignInUseCase {
    private final GenerateJwtTokenCommand generateJwtTokenCommand;
    private final CreateUserCommand createUserCommand;

    public AuthResponse exchangeTokenForNewUser(SignInRequest request) {

        User user = createUserCommand.createNewUser(request.getToken(), request.getNickname());

        AuthResponse response = generateJwtTokenCommand.execute(user);

        log.info("토큰 교환 성공 - User ID: {}", user.getId());
        return response;
    }
}