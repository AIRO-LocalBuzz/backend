package backend.airo.infra.open_api.tour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenApiKoreaTourResponse<T>(
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
            @JsonProperty("items")      Items<T> items,
            @JsonProperty("numOfRows")  Integer numOfRows,
            @JsonProperty("pageNo")     Integer pageNo,
            @JsonProperty("totalCount") Long totalCount
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Items<T>(
            @JsonProperty("item")
            @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            List<T> item
    ) { }

    public Items<T> items() {
        return response.body().items;
    }

    /** 편의 메서드 */
    public List<T> item() {
        return (response == null || response.body() == null) ? List.of() : response.body().items.item;
    }

    public Body<T> body() {
        return response().body();
    }

    public Header header() {
        return response.header();
    }
}
