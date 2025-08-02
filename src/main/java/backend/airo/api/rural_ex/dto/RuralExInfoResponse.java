package backend.airo.api.rural_ex.dto;

import backend.airo.domain.rural_ex.RuralEx;
import lombok.Builder;

@Builder
public record RuralExInfoResponse(

        Long id,
        String vilageName,
        String experienceType,
        String experienceName,
        String region,
        String manager,
        String managerPhoneNumber,
        String roadAddr,
        String lotAddr
) {

    public static RuralExInfoResponse create(RuralEx ruralEx) {
        return RuralExInfoResponse.builder()
                .id(ruralEx.getId())
                .vilageName(ruralEx.getExprnVilageNm())
                .experienceType(ruralEx.getExprnSe())
                .experienceName(ruralEx.getExprnCn())
                .region(addMegaNameCityName(ruralEx.getCtprvnNm(), ruralEx.getSignguNm()))
                .manager(ruralEx.getRprsntvNm())
                .managerPhoneNumber(ruralEx.getPhoneNumber())
                .roadAddr(ruralEx.getRuralExAddress().rdnmadr())
                .lotAddr(ruralEx.getRuralExAddress().lnmadr())
                .build();
    }

    private static String addMegaNameCityName(String megaName, String cityName) {
        return megaName + " " + cityName;
    }

}
