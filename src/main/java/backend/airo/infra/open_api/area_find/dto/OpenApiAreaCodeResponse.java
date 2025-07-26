package backend.airo.infra.open_api.area_find.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenApiAreaCodeResponse<T>(
        @JsonProperty("header") Header header,
        @JsonProperty("body") Body<T> body
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Header(
            @JsonProperty("description") String description,
            @JsonProperty("columns") List<String> columns,
            @JsonProperty("resultCode") String resultCode,
            @JsonProperty("resultMsg") String resultMsg
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Body<T>(
            @JsonProperty("items") List<T> items
    ) {}

    public List<T> items() {
        return body.items();
    }
}

