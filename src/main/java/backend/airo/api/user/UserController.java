package backend.airo.api.user;

import backend.airo.api.global.dto.Response;
import backend.airo.api.user.dto.LoginRequest;
import backend.airo.api.user.dto.UserResponse;
import backend.airo.application.user.usecase.UserUseCase;
import backend.airo.domain.user.User;
import backend.airo.domain.user.query.GetUserByFirebaseUidQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserUseCase userUseCase;

    /**
     * Firebase 소셜 로그인 / 회원가입
     */
    @PostMapping("/login")
    public Response<UserResponse> login(@RequestBody LoginRequest request) {
        log.info("소셜 로그인 요청");
        
        User user = userUseCase.loginWithFirebase(request.getIdToken());
        UserResponse response = new UserResponse(user);
        
        log.info("로그인 성공: userId={}, firebaseUid={}", user.getId(), user.getFirebaseUid());
        return Response.success(response);
    }

    /**
     * 사용자 정보 조회
     */
    @GetMapping("/me")
    public Response<UserResponse> getCurrentUser(@RequestParam Long userId) {
        log.info("사용자 정보 조회: userId={}", userId);
        
        User user = userUseCase.getUserById(userId);
        UserResponse response = new UserResponse(user);
        
        return Response.success(response);
    }

    /**
     * Firebase UID로 사용자 조회 (내부 용도)
     */
    @GetMapping("/firebase/{firebaseUid}")
    public Response<UserResponse> getUserByFirebaseUid(@PathVariable String firebaseUid) {
        log.info("Firebase UID로 사용자 조회: firebaseUid={}", firebaseUid);
        
        GetUserByFirebaseUidQuery query = new GetUserByFirebaseUidQuery(firebaseUid);
        User user = userUseCase.getUserByFirebaseUid(query);
        UserResponse response = new UserResponse(user);
        
        return Response.success(response);
    }
}