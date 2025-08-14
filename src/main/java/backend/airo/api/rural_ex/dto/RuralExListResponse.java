package backend.airo.api.rural_ex.dto;

import backend.airo.domain.rural_ex.RuralEx;
import lombok.Builder;

@Builder
public record RuralExListResponse(

        Long id,
        String vilageName,
        String experienceType,
        String region
) {

    public static RuralExListResponse create(RuralEx ruralEx) {
        return RuralExListResponse.builder()
                .id(ruralEx.id())
                .vilageName(ruralEx.exprnVilageNm())
                .experienceType(ruralEx.exprnSe())
                .region(addMegaNameCityName(ruralEx.ctprvnNm(), ruralEx.signguNm()))
                .build();
    }

    private static String addMegaNameCityName(String megaName, String cityName) {
        return megaName + " " + cityName;
    }

}
