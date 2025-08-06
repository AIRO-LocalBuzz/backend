package backend.airo.application.clure_fatvl.dto;



import java.util.List;

public record OpenApiClutrFatvlResponse(
        List<OpenApiClutrFatvlInfo> openApiClutrFatvlInfos,
        String resultCode,
        String numOfRows,
        String pageNo,
        String totalCount
) {
}
