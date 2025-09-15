package backend.airo.domain.clure_fatvl;

import backend.airo.application.clure_fatvl.dto.OpenApiClutrFatvlInfo;
import backend.airo.domain.clure_fatvl.vo.Address;
import backend.airo.domain.clure_fatvl.vo.FestivalPeriod;
import backend.airo.domain.clure_fatvl.vo.GeoPoint;
import jakarta.persistence.Embedded;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ClutrFatvl(
        String id,
        String fstvlNm,
        String opar,
        String fstvlCo,
        @Embedded FestivalPeriod period,
        @Embedded GeoPoint location,
        @Embedded Address address,
        String mnnstNm,
        String auspcInsttNm,
        String suprtInsttNm,
        String phoneNumber,
        String homepageUrl,
        String relateInfo,
        LocalDate referenceDate,
        String insttCode,
        String insttNm)
{
    public static ClutrFatvl create(OpenApiClutrFatvlInfo dto, Long megaCode, Long cityCode) {
        return ClutrFatvl.builder()
                .fstvlNm(dto.fstvlNm())
                .opar(dto.opar())
                .fstvlCo(dto.fstvlCo())
                .period(new FestivalPeriod(dto.start(), dto.end()))
                .location(new GeoPoint(dto.lat(), dto.lon()))
                .address(new Address(dto.road(), dto.lot(), String.valueOf(megaCode), String.valueOf(cityCode)))
                .mnnstNm(dto.mnnstNm())
                .auspcInsttNm(dto.auspcInsttNm())
                .suprtInsttNm(dto.suprtInsttNm())
                .phoneNumber(dto.phoneNumber())
                .homepageUrl(dto.homepageUrl())
                .relateInfo(dto.relateInfo())
                .referenceDate(dto.referenceDate())
                .insttCode(dto.insttCode())
                .insttNm(dto.insttNm())
                .build();
    }
}

