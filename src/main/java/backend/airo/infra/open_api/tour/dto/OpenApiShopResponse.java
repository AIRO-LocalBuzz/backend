package backend.airo.infra.open_api.tour.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OpenApiShopResponse<T>(
        @JsonProperty("header") Header header,
        @JsonProperty("body") Body<T> body
) {
    public record Header(
            @JsonProperty("description") String description,
            @JsonProperty("columns") List<String> columns,
            @JsonProperty("stdrYm") String stdrYm,
            @JsonProperty("resultCode") String resultCode,
            @JsonProperty("resultMsg") String resultMsg
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Body<T>(
            @JsonProperty("items") T items,
            @JsonProperty("numOfRows") int numOfRows,
            @JsonProperty("pageNo") int pageNo,
            @JsonProperty("totalCount") long totalCount
    ) {}

    public T getItems() {
        return body.items();
    }
}