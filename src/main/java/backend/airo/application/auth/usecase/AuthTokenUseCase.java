package backend.airo.application.auth.usecase;

import backend.airo.api.auth.dto.AuthTokenRequest;
import backend.airo.api.auth.dto.AuthTokenResponse;
import backend.airo.domain.auth.oauth2.command.ExchangeTokenCommand;
import backend.airo.domain.auth.oauth2.query.GetUserByTempCodeQuery;
import backend.airo.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthTokenUseCase {
    private final GetUserByTempCodeQuery getUserByTempCodeQuery;
    private final ExchangeTokenCommand exchangeTokenCommand;

    public AuthTokenResponse exchangeToken(AuthTokenRequest request) {

        User user = getUserByTempCodeQuery.getUserByTempCode(request.getCode());

        AuthTokenResponse response = exchangeTokenCommand.execute(user.getId(), request.getCode());

        log.info("토큰 교환 성공 - User ID: {}", user.getId());
        return response;
    }
}