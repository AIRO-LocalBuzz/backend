package backend.airo.domain.rural_ex.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public record RuralExLocation(
        double latitude,
        double longitude
) {
}
