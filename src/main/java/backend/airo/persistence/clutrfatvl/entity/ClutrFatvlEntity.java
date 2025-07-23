package backend.airo.persistence.clutrfatvl.entity;

import backend.airo.infra.open_api.vo.ClutrFatvlInfo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private Long id = 0L;
    private String fstvlNm;
    private String opar;
    private LocalDate fstvlStartDate;
    private LocalDate fstvlEndDate;
    private String fstvlCo;
    private String mnnstNm;
    private String auspcInsttNm;
    private String suprtInsttNm;
    private String phoneNumber;
    private String homepageUrl;
    private String relateInfo;
    private String rdnmadr;
    private String lnmadr;
    private Double latitude;
    private Double longitude;
    private LocalDate referenceDate;
    private String insttCode;
    private String insttNm;

    @Builder
    public ClutrFatvlEntity(String fstvlNm, String opar, LocalDate fstvlStartDate, LocalDate fstvlEndDate, String fstvlCo, String mnnstNm, String auspcInsttNm, String suprtInsttNm, String phoneNumber, String homepageUrl, String relateInfo, String rdnmadr, String lnmadr, Double latitude, Double longitude, LocalDate referenceDate, String insttCode, String insttNm) {
        this.fstvlNm = fstvlNm;
        this.opar = opar;
        this.fstvlStartDate = fstvlStartDate;
        this.fstvlEndDate = fstvlEndDate;
        this.fstvlCo = fstvlCo;
        this.mnnstNm = mnnstNm;
        this.auspcInsttNm = auspcInsttNm;
        this.suprtInsttNm = suprtInsttNm;
        this.phoneNumber = phoneNumber;
        this.homepageUrl = homepageUrl;
        this.relateInfo = relateInfo;
        this.rdnmadr = rdnmadr;
        this.lnmadr = lnmadr;
        this.latitude = latitude;
        this.longitude = longitude;
        this.referenceDate = referenceDate;
        this.insttCode = insttCode;
        this.insttNm = insttNm;
    }

    public static ClutrFatvlEntity create(ClutrFatvlInfo clutrFatvlInfo) {
        return ClutrFatvlEntity.builder()
                .fstvlNm(clutrFatvlInfo.fstvlNm())
                .opar(clutrFatvlInfo.opar())
                .fstvlStartDate(clutrFatvlInfo.fstvlStartDate())
                .fstvlEndDate(clutrFatvlInfo.fstvlEndDate())
                .fstvlCo(clutrFatvlInfo.fstvlCo())
                .mnnstNm(clutrFatvlInfo.mnnstNm())
                .auspcInsttNm(clutrFatvlInfo.auspcInsttNm())
                .suprtInsttNm(clutrFatvlInfo.suprtInsttNm())
                .phoneNumber(clutrFatvlInfo.phoneNumber())
                .homepageUrl(clutrFatvlInfo.homepageUrl())
                .relateInfo(clutrFatvlInfo.relateInfo())
                .rdnmadr(clutrFatvlInfo.rdnmadr())
                .lnmadr(clutrFatvlInfo.lnmadr())
                .latitude(clutrFatvlInfo.latitude())
                .longitude(clutrFatvlInfo.longitude())
                .referenceDate(clutrFatvlInfo.referenceDate())
                .insttCode(clutrFatvlInfo.instt_code())
                .insttNm(clutrFatvlInfo.instt_nm())
                .build();
    }
}
