package backend.airo.domain.rural_ex.vo;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public record RuralExAdmin(
        LocalDate appnDate,
        String institutionNm,
        String insttCode,
        String insttNm
) {
}
