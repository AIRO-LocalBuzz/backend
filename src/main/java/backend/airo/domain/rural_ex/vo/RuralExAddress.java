package backend.airo.domain.rural_ex.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public record RuralExAddress(
        String rdnmadr,
        String lnmadr

) {
}
