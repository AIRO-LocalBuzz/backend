//package backend.airo.api.post.dto;
//
//package backend.airo.api.global.dto;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import io.swagger.v3.oas.annotations.media.Schema;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Schema(description = "API 에러 응답")
//public record ErrorResponse(
//        @Schema(description = "요청 성공 여부", example = "false")
//        boolean success,
//
//        @Schema(description = "에러 정보")
//        ErrorDetail error,
//
//        @Schema(description = "응답 시간", example = "2025-08-06T15:30:45")
//        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
//        LocalDateTime timestamp
//) {
//    public static ErrorResponse of(String code, String message) {
//        return new ErrorResponse(
//                false,
//                new ErrorDetail(code, message, List.of()),
//                LocalDateTime.now()
//        );
//    }
//
//    public static ErrorResponse of(String code, String message, List<String> details) {
//        return new ErrorResponse(
//                false,
//                new ErrorDetail(code, message, details),
//                LocalDateTime.now()
//        );
//    }
//
//    @Schema(description = "에러 상세 정보")
//    public record ErrorDetail(
//            @Schema(description = "에러 코드", example = "POST_001")
//            String code,
//
//            @Schema(description = "에러 메시지", example = "게시물을 찾을 수 없습니다")
//            String message,
//
//            @Schema(description = "상세 에러 정보", example = "[\"title: 제목은 필수입니다\"]")
//            List<String> details
//    ) {}
//}