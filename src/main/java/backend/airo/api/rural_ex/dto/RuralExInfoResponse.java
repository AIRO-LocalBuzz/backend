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
                .id(ruralEx.id())
                .vilageName(ruralEx.exprnVilageNm())
                .experienceType(ruralEx.exprnSe())
                .experienceName(ruralEx.exprnCn())
                .region(addMegaNameCityName(ruralEx.ctprvnNm(), ruralEx.signguNm()))
                .manager(ruralEx.rprsntvNm())
                .managerPhoneNumber(ruralEx.phoneNumber())
                .roadAddr(ruralEx.ruralExAddress().rdnmadr())
                .lotAddr(ruralEx.ruralExAddress().lnmadr())
                .build();
    }

    private static String addMegaNameCityName(String megaName, String cityName) {
        return megaName + " " + cityName;
    }

}
