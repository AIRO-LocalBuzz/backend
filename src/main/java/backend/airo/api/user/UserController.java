package backend.airo.api.user;

import backend.airo.api.global.dto.Response;
import backend.airo.api.user.dto.CheckNicknameResponse;
import backend.airo.api.user.dto.SetNicknameRequest;
import backend.airo.api.user.dto.UserResponse;
import backend.airo.application.user.usecase.UserUseCase;
import backend.airo.domain.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @GetMapping("/nickname/check")
    public Response<CheckNicknameResponse> checkNicknameAvailability(@RequestParam String nickname) {
        boolean available = userUseCase.checkNicknameAvailability(nickname);
        CheckNicknameResponse response = available ? 
            CheckNicknameResponse.available() : 
            CheckNicknameResponse.unavailable();
        return Response.success(response);
    }

    // For first-time users creating their nickname
    @PostMapping("/nickname")
    public Response<UserResponse> createUserWithNickname(@Valid @RequestBody SetNicknameRequest request) {
        User user = userUseCase.createUser(request.getNickname());
        return Response.success(UserResponse.from(user));
    }

    // For updating existing user's nickname
    @PutMapping("/{userId}/nickname")
    public Response<UserResponse> updateNickname(
            @PathVariable Long userId, 
            @Valid @RequestBody SetNicknameRequest request) {
        User user = userUseCase.updateNickname(userId, request.getNickname());
        return Response.success(UserResponse.from(user));
    }

    @GetMapping("/{userId}")
    public Response<UserResponse> getUser(@PathVariable Long userId) {
        User user = userUseCase.getUser(userId);
        return Response.success(UserResponse.from(user));
    }
}