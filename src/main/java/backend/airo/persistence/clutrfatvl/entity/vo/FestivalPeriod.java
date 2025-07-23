package backend.airo.persistence.clutrfatvl.entity.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public record FestivalPeriod(
        @Column(name = "start_date") LocalDate start,
        @Column(name = "end_date")   LocalDate end
) {
}