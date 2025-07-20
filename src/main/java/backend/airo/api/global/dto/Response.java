package backend.airo.api.global.dto;

import backend.airo.common.exception.ErrorReason;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private String path;

    private String responseCode;
    private String message;

    private T result;

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
