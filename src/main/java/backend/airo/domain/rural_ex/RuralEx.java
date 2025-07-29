package backend.airo.domain.rural_ex;

import backend.airo.domain.rural_ex.vo.RuralExAddress;
import backend.airo.domain.rural_ex.vo.RuralExAdmin;
import backend.airo.domain.rural_ex.vo.RuralExLocation;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RuralEx {

    private Long id;

    private final String exprnSe;

    private final String exprnCn;

    private final String holdFclty;

    private final String exprnAr;

    private final String exprnPicUrl;

    private final String exprnVilageNm;

    private final String ctprvnNm;

    private final String signguNm;

    private final String rprsntvNm;

    private final String phoneNumber;

    private final RuralExAddress ruralExAddress;

    private final RuralExAdmin ruralExAdmin;

    private final RuralExLocation ruralExLocation;

    private final LocalDate referenceDate;

    public RuralEx(Long id,
                   String exprnSe,
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
        this.id = id;
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

}
