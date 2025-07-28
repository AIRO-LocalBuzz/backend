package backend.airo.api.global.swagger;

import backend.airo.api.global.dto.Response;
import backend.airo.api.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestHeader;

public interface UserControllerSwagger {

    @Operation(summary = "사용자 마이페이지 조회", description = "사용자 마이페이지 조회 API")
    @ApiResponse(
            responseCode = "200",
            description = "사용자 마이페이지 조회 성공",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
    )
    Response<UserResponse> getMyPage(@RequestHeader(value = "Authorization", required = true) String bearerToken);

}
