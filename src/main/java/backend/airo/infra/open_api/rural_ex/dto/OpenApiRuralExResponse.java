package backend.airo.infra.open_api.rural_ex.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenApiRuralExResponse<T>(
        @JsonProperty("response") Response<T> response
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response<T>(
            @JsonProperty("header") Header header,
            @JsonProperty("body") Body<T> body
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Header(
            @JsonProperty("resultCode") String resultCode,
            @JsonProperty("resultMsg")  String resultMsg
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Body<T>(
            @JsonProperty("items")      List<T> items,
            @JsonProperty("numOfRows")  String numOfRows,   // JSON이 문자열이므로 String으로 받는 게 안전
            @JsonProperty("pageNo")     String pageNo,
            @JsonProperty("totalCount") String totalCount
    ) {
    }

    /** 편의 메서드 */
    public List<T> items() {
        return response().body().items();
    }
}
