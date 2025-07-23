package backend.airo.persistence.clutrfatvl.entity;

import backend.airo.infra.clure_fatvl_open_api.vo.ClutrFatvlInfo;
import backend.airo.persistence.clutrfatvl.entity.vo.Address;
import backend.airo.persistence.clutrfatvl.entity.vo.FestivalPeriod;
import backend.airo.persistence.clutrfatvl.entity.vo.GeoPoint;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ClutrFatvlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fstvlNm;

    private String opar;

    private String fstvlCo;

    @Embedded
    private FestivalPeriod period;

    @Embedded
    private GeoPoint location;

    @Embedded
    private Address address;

    private String mnnstNm;

    private String auspcInsttNm;

    private String suprtInsttNm;

    private String phoneNumber;

    private String homepageUrl;

    private String relateInfo;

    private LocalDate referenceDate;

    private String insttCode;

    private String insttNm;

    @Builder
    public ClutrFatvlEntity(
            String fstvlNm, String opar, String fstvlCo,
            FestivalPeriod period, GeoPoint location, Address address,
            String mnnstNm, String auspcInsttNm, String suprtInsttNm,
            String phoneNumber, String homepageUrl, String relateInfo,
            LocalDate referenceDate, String insttCode, String insttNm) {

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

    public static ClutrFatvlEntity create(ClutrFatvlInfo dto) {
        return ClutrFatvlEntity.builder()
                .fstvlNm(dto.fstvlNm())
                .opar(dto.opar())
                .fstvlCo(dto.fstvlCo())
                .period(new FestivalPeriod(dto.fstvlStartDate(), dto.fstvlEndDate()))
                .location(new GeoPoint(dto.latitude(), dto.longitude()))
                .address(new Address(dto.rdnmadr(), dto.lnmadr()))
                .mnnstNm(dto.mnnstNm())
                .auspcInsttNm(dto.auspcInsttNm())
                .suprtInsttNm(dto.suprtInsttNm())
                .phoneNumber(dto.phoneNumber())
                .homepageUrl(dto.homepageUrl())
                .relateInfo(dto.relateInfo())
                .referenceDate(dto.referenceDate())
                .insttCode(dto.instt_code())
                .insttNm(dto.instt_nm())
                .build();
    }
}

