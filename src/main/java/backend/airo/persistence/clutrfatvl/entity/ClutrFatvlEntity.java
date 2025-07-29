package backend.airo.persistence.clutrfatvl.entity;

import backend.airo.domain.clure_fatvl.ClutrFatvl;
import backend.airo.infra.open_api.clure_fatvl.vo.ClutrFatvlInfo;
import backend.airo.domain.clure_fatvl.vo.Address;
import backend.airo.domain.clure_fatvl.vo.FestivalPeriod;
import backend.airo.domain.clure_fatvl.vo.GeoPoint;
import backend.airo.persistence.abstracts.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ClutrFatvlEntity extends BaseEntity {

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

    public static ClutrFatvlEntity toEntity(ClutrFatvl dto) {
        return ClutrFatvlEntity.builder()
                .fstvlNm(dto.getFstvlNm())
                .opar(dto.getOpar())
                .fstvlCo(dto.getFstvlCo())
                .period(new FestivalPeriod(dto.getPeriod().start(), dto.getPeriod().end()))
                .location(new GeoPoint(dto.getLocation().lat(), dto.getLocation().lon()))
                .address(new Address(dto.getAddress().road(), dto.getAddress().lot(), dto.getAddress().megaCodeId(), dto.getAddress().ctprvnCodeId()))
                .mnnstNm(dto.getMnnstNm())
                .auspcInsttNm(dto.getAuspcInsttNm())
                .suprtInsttNm(dto.getSuprtInsttNm())
                .phoneNumber(dto.getPhoneNumber())
                .homepageUrl(dto.getHomepageUrl())
                .relateInfo(dto.getRelateInfo())
                .referenceDate(dto.getReferenceDate())
                .insttCode(dto.getInsttCode())
                .insttNm(dto.getInsttNm())
                .build();
    }

    public static ClutrFatvl toDomain(ClutrFatvlInfo dto, String megaCodeId, String ctprvnCodeId) {
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

    public static ClutrFatvl toDomain(ClutrFatvlEntity clutrFatvlEntity) {
        FestivalPeriod period = clutrFatvlEntity.period;
        GeoPoint location = clutrFatvlEntity.location;
        Address address = clutrFatvlEntity.address;
        return ClutrFatvl.builder()
                .id(clutrFatvlEntity.id)
                .fstvlNm(clutrFatvlEntity.fstvlNm)
                .opar(clutrFatvlEntity.opar)
                .fstvlCo(clutrFatvlEntity.fstvlCo)
                .period(new FestivalPeriod(period.start(), period.end()))
                .location(new GeoPoint(location.lat(), location.lon()))
                .address(new Address(address.road(), address.lot(), address.megaCodeId(), address.ctprvnCodeId()))
                .mnnstNm(clutrFatvlEntity.mnnstNm)
                .auspcInsttNm(clutrFatvlEntity.auspcInsttNm)
                .suprtInsttNm(clutrFatvlEntity.suprtInsttNm)
                .phoneNumber(clutrFatvlEntity.phoneNumber)
                .homepageUrl(clutrFatvlEntity.homepageUrl)
                .relateInfo(clutrFatvlEntity.relateInfo)
                .referenceDate(clutrFatvlEntity.referenceDate)
                .insttCode(clutrFatvlEntity.insttCode)
                .insttNm(clutrFatvlEntity.insttNm)
                .build();
    }

}

