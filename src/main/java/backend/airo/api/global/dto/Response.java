package backend.airo.api.global.dto;

import backend.airo.common.exception.ErrorReason;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    @Schema(description = "요청 경로", example = "/api/v1/resource")
    private String path;

    @Schema(example = "400 BAD_REQUEST | 401 UNAUTHORIZED | 404 NOT_FOUND | 500 INTERNAL_SERVER_ERROR")
    private String responseCode;

    @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다.")
    private String message;

    @Schema(description = "응답 결과", example = "{...}")
    private T result;

    @Schema(description = "응답 시간", example = "2023-10-01T12:00:00")
    private LocalDateTime timeStamp;

    private Response(String responseCode, T result) {
        this.responseCode = responseCode;
        this.result = result;
    }

    private Response(String responseCode, String message) {
        this.responseCode = responseCode;
        this.message = message;
    }

    public Response(String path, String errorCode, String message) {
        this.path = path;
        this.responseCode = errorCode;
        this.message = message;
        this.timeStamp = LocalDateTime.now();
    }

    public static <T> Response<T> success(T result) {
        return new Response<>("SUCCESS", result);
    }

    public static Response<Void> success() {
        return new Response<>("SUCCESS", null);
    }

    public static Response<Void> success(String message) {
        return new Response<>("SUCCESS", message);
    }

    public static Response<Void> error(ErrorReason errorReason, String path, String message) {
        return new Response<>(path, errorReason.errorCode(), message);
    }

    public static Response<Void> error(String errorCode, String message) {
        return new Response<>(errorCode, message);
    }
}
