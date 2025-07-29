package backend.airo.domain.clure_fatvl;

import backend.airo.infra.open_api.clure_fatvl.vo.ClutrFatvlInfo;
import backend.airo.domain.clure_fatvl.vo.Address;
import backend.airo.domain.clure_fatvl.vo.FestivalPeriod;
import backend.airo.domain.clure_fatvl.vo.GeoPoint;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ClutrFatvl {

    private Long id;

    private final String fstvlNm;

    private final String opar;

    private final String fstvlCo;

    @Embedded
    private final FestivalPeriod period;

    @Embedded
    private final GeoPoint location;

    @Embedded
    private final Address address;

    private final String mnnstNm;

    private final String auspcInsttNm;

    private final String suprtInsttNm;

    private final String phoneNumber;

    private final String homepageUrl;

    private final String relateInfo;

    private final LocalDate referenceDate;

    private final String insttCode;

    private final String insttNm;

    @Builder
    public ClutrFatvl(
            Long id, String fstvlNm, String opar, String fstvlCo,
            FestivalPeriod period, GeoPoint location, Address address,
            String mnnstNm, String auspcInsttNm, String suprtInsttNm,
            String phoneNumber, String homepageUrl, String relateInfo,
            LocalDate referenceDate, String insttCode, String insttNm) {
        this.id = id;
        this.fstvlNm = fstvlNm;
        this.opar = opar;
        this.fstvlCo = fstvlCo;
        this.period = period;
        this.location = location;
        this.address = address;
        this.mnnstNm = mnnstNm;
        this.auspcInsttNm = auspcInsttNm;
        this.suprtInsttNm = suprtInsttNm;
        this.phoneNumber = phoneNumber;
        this.homepageUrl = homepageUrl;
        this.relateInfo = relateInfo;
        this.referenceDate = referenceDate;
        this.insttCode = insttCode;
        this.insttNm = insttNm;
    }

    public static ClutrFatvl create(ClutrFatvlInfo dto, String megaCodeId, String ctprvnCodeId) {
        return ClutrFatvl.builder()
                .fstvlNm(dto.fstvlNm())
                .opar(dto.opar())
                .fstvlCo(dto.fstvlCo())
                .period(new FestivalPeriod(dto.fstvlStartDate(), dto.fstvlEndDate()))
                .location(new GeoPoint(dto.latitude(), dto.longitude()))
                .address(new Address(dto.rdnmadr(), dto.lnmadr(), megaCodeId, ctprvnCodeId))
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

