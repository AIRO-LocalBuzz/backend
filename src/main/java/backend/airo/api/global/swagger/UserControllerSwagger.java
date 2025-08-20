package backend.airo.api.global.swagger;

import backend.airo.api.annotation.UserPrincipal;
import backend.airo.api.global.dto.Response;
import backend.airo.api.user.dto.UpdateUserInfoRequest;
import backend.airo.api.user.dto.UserResponse;
import backend.airo.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserControllerSwagger {

    @Operation(summary = "사용자 마이페이지 조회", description = "사용자 마이페이지 조회 API")
    @ApiResponse(
            responseCode = "200",
            description = "사용자 마이페이지 조회 성공",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
    )
    Response<UserResponse> getMyPage(@UserPrincipal User user);


    @Operation(summary = "사용자 정보 수정", description = "사용자 정보 수정 API")
    @Parameters({
            @Parameter(name = "name", description = "이름", example = "홍길동"),
            @Parameter(name = "nickname", description = "닉네임", example = "스타크래프트"),
            @Parameter(name = "phoneNumber", description = "휴대폰 번호", example = "010-1234-1234"),
            @Parameter(name = "birthDate", description = "생년월일", example = "2000-05-04")
    })
    @ApiResponse(
            responseCode = "200",
            description = "사용자 마이페이지 조회 성공",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
    )
    Response<UserResponse> updateMyPage(
            @UserPrincipal User user,
            @RequestBody UpdateUserInfoRequest updateUserInfoRequest
    );

    @Operation(summary = "사용자 탈퇴", description = "사용자 탈퇴 API - 계정과 관련된 모든 데이터가 삭제됩니다")
    @ApiResponse(
            responseCode = "200",
            description = "사용자 탈퇴 성공"
    )
    @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자"
    )
    Response<Void> deleteUser(@UserPrincipal User user);
}
