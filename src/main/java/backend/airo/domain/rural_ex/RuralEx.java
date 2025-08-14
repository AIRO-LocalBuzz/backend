package backend.airo.domain.rural_ex;

import backend.airo.domain.rural_ex.vo.RuralExAddress;
import backend.airo.domain.rural_ex.vo.RuralExAdmin;
import backend.airo.domain.rural_ex.vo.RuralExLocation;

import java.time.LocalDate;

public record RuralEx(
        Long id,
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
        LocalDate referenceDate
) {

}
