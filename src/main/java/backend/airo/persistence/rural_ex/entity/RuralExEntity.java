package backend.airo.persistence.rural_ex.entity;

import backend.airo.domain.rural_ex.RuralEx;
import backend.airo.domain.rural_ex.vo.RuralExAddress;
import backend.airo.domain.rural_ex.vo.RuralExAdmin;
import backend.airo.domain.rural_ex.vo.RuralExLocation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RuralExEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String exprnSe;

    @Column(columnDefinition = "TEXT")
    private String exprnCn;

    private String holdFclty;

    private String exprnAr;

    private String exprnPicUrl;

    private String exprnVilageNm;

    private String ctprvnNm;

    private String signguNm;

    private String rprsntvNm;

    private String phoneNumber;

    @Embedded
    private RuralExAddress ruralExAddress;

    @Embedded
    private RuralExAdmin ruralExAdmin;

    @Embedded
    private RuralExLocation ruralExLocation;

    private LocalDate referenceDate;

    @Builder
    public RuralExEntity(String exprnSe,
                         String exprnCn,
                         String holdFclty,
                         String exprnAr,
                         String exprnPicUrl,
                         String exprnVilageNm,
                         String ctprvnNm,
                         String signguNm,
                         String rprsntvNm,
                         String phoneNumber,
                         RuralExAddress ruralExAddress,
                         RuralExAdmin ruralExAdmin,
                         RuralExLocation ruralExLocation,
                         LocalDate referenceDate) {
        this.exprnSe = exprnSe;
        this.exprnCn = exprnCn;
        this.holdFclty = holdFclty;
        this.exprnAr = exprnAr;
        this.exprnPicUrl = exprnPicUrl;
        this.exprnVilageNm = exprnVilageNm;
        this.ctprvnNm = ctprvnNm;
        this.signguNm = signguNm;
        this.rprsntvNm = rprsntvNm;
        this.phoneNumber = phoneNumber;
        this.ruralExAddress = ruralExAddress;
        this.ruralExAdmin = ruralExAdmin;
        this.ruralExLocation = ruralExLocation;
        this.referenceDate = referenceDate;
    }

    public static RuralExEntity toEntity(RuralEx ruralEx) {
        return RuralExEntity.builder()
                .exprnSe(ruralEx.exprnSe())
                .exprnCn(ruralEx.exprnCn())
                .holdFclty(ruralEx.holdFclty())
                .exprnAr(ruralEx.exprnAr())
                .exprnPicUrl(ruralEx.exprnPicUrl())
                .exprnVilageNm(ruralEx.exprnVilageNm())
                .ctprvnNm(ruralEx.ctprvnNm())
                .signguNm(ruralEx.signguNm())
                .rprsntvNm(ruralEx.rprsntvNm())
                .phoneNumber(ruralEx.phoneNumber())
                .ruralExAddress(ruralEx.ruralExAddress())
                .ruralExAdmin(ruralEx.ruralExAdmin())
                .ruralExLocation(ruralEx.ruralExLocation())
                .referenceDate(ruralEx.referenceDate())
                .build();
    }

    public static RuralEx toDomain(RuralExEntity ruralExEntity) {
        return new RuralEx(
                ruralExEntity.id,
                ruralExEntity.exprnSe,
                ruralExEntity.exprnCn,
                ruralExEntity.holdFclty,
                ruralExEntity.exprnAr,
                ruralExEntity.exprnPicUrl,
                ruralExEntity.exprnVilageNm,
                ruralExEntity.ctprvnNm,
                ruralExEntity.signguNm,
                ruralExEntity.rprsntvNm,
                ruralExEntity.phoneNumber,
                ruralExEntity.ruralExAddress,
                ruralExEntity.ruralExAdmin,
                ruralExEntity.ruralExLocation,
                ruralExEntity.referenceDate
        );
    }
}
