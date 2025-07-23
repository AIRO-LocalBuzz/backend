package backend.airo.infra.open_api.clure_fatvl.dto;//package garbege.open_api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//TODO 전국문화축제 정보는 아직 모름 확인 필요
public record OpenApiClureFatvlResponse<T>(
        @JsonProperty("response") Response<T> response) {

    public record Response<T>(
            @JsonProperty("header") Header header,
            @JsonProperty("body") Body<T> body) {

        public record Header(
                @JsonProperty("description") String description,
                @JsonProperty("columns")     String columns,
                @JsonProperty("stdrYm")      String stdrYm,
                @JsonProperty("resultCode")  String resultCode,
                @JsonProperty("resultMsg")   String resultMsg) {}

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Body<T>(
                @JsonProperty("items") List<T> items,
                @JsonProperty("numOfRows") int numOfRows,
                @JsonProperty("pageNo")    int pageNo,
                @JsonProperty("totalCount") long totalCount) {

            public record List<T>(
                    @JsonProperty("item") T item) {}
        }
    }

    public T getItems() {
        return response.body.items.item;
    }
}