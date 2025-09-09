package backend.airo.application.clure_fatvl.dto;



import java.util.List;

public record OpenApiClutrFatvlResponse(
        List<OpenApiClutrFatvl> openApiClutrFatvls,
        String resultCode,
        String numOfRows,
        String pageNo,
        String totalCount
) {
}
