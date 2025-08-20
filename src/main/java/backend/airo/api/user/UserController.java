package backend.airo.api.user;

import backend.airo.api.annotation.UserPrincipal;
import backend.airo.api.global.dto.Response;
import backend.airo.api.global.swagger.UserControllerSwagger;
import backend.airo.api.user.dto.UpdateUserInfoRequest;
import backend.airo.api.user.dto.UserResponse;
import backend.airo.application.user.usecase.UserUseCase;
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

    private final UserUseCase userUseCase;

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public Response<UserResponse> getMyPage(@UserPrincipal User user) {
        return Response.success(UserResponse.create(user));
    }

    @PatchMapping()
    @PreAuthorize("isAuthenticated()")
    public Response<UserResponse> updateMyPage(
            @UserPrincipal User user,
            @RequestBody UpdateUserInfoRequest updateUserInfoRequest
    ) {
        User updateUser = userUseCase.updateUser(
                user.getId(),
                updateUserInfoRequest.getName(),
                updateUserInfoRequest.getNickname(),
                updateUserInfoRequest.getPhoneNumber(),
                updateUserInfoRequest.getBirthDate()
        );
        return Response.success(UserResponse.create(updateUser));
    }

    @DeleteMapping()
    @PreAuthorize("isAuthenticated()")
    public Response<Void> deleteUser(@UserPrincipal User user) {
        userUseCase.deleteUser(user.getId());
        return Response.success(null);
    }
}
