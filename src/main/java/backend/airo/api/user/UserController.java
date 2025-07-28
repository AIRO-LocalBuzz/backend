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
        Long userId = jwtTokenProvider.getUserIdFromToken(bearerToken);
        User user = userUseCase.getUserById(userId);
        return Response.success(UserResponse.create(user));
    }

    @PatchMapping()
    @PreAuthorize("isAuthenticated()")
    public Response<UserResponse> updateMyPage(
            @RequestHeader(value = "Authorization", required = true) String bearerToken,
            @RequestBody UpdateUserInfoRequest updateUserInfoRequest
    ) {
        Long userId = jwtTokenProvider.getUserIdFromToken(bearerToken);
//        User user = userUseCase.updateUser(userId, updateUserInfoRequest.getNickname(), updateUserInfoRequest.getEmail());
        return Response.success(UserResponse.create(null));
    }

}
