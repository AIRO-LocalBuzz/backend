package backend.airo.api.user;

import backend.airo.api.global.dto.Response;
import backend.airo.api.global.swagger.UserControllerSwagger;
import backend.airo.api.user.dto.UpdateUserInfoRequest;
import backend.airo.api.user.dto.UserResponse;
import backend.airo.application.user.usecase.UserUseCase;
import backend.airo.common.jwt.JwtTokenProvider;
import backend.airo.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController implements UserControllerSwagger {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserUseCase userUseCase;

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public Response<UserResponse> getMyPage(@RequestHeader(value = "Authorization", required = true) String bearerToken) {
        if (!bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header format");
        }

        String accessToken = bearerToken.substring(7);
        if (accessToken.isEmpty()) {
            throw new IllegalArgumentException("Access token is empty");
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
        User user = userUseCase.getUserById(userId);
        return Response.success(UserResponse.create(user));
    }

    @PatchMapping()
    @PreAuthorize("isAuthenticated()")
    public Response<UserResponse> updateMyPage(
            @RequestHeader(value = "Authorization", required = true) String bearerToken,
            @RequestBody UpdateUserInfoRequest updateUserInfoRequest
    ) {
        if (!bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header format");
        }

        String accessToken = bearerToken.substring(7);
        if (accessToken.isEmpty()) {
            throw new IllegalArgumentException("Access token is empty");
        }
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
        User user = userUseCase.updateUser(
                userId,
                updateUserInfoRequest.getName(),
                updateUserInfoRequest.getNickname(),
                updateUserInfoRequest.getPhoneNumber(),
                updateUserInfoRequest.getBirthDate()
        );
        return Response.success(UserResponse.create(user));
    }

}
