package backend.airo.domain.clure_fatvl.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public record FestivalPeriod(
        @Column(name = "start_date") LocalDate start,
        @Column(name = "end_date")   LocalDate end
) {

    //행사가 지금 진행 중인가에 대한 체크
    public boolean progressCheck() {
        LocalDate now = LocalDate.now();
        return !now.isBefore(start) && !now.isAfter(end);
    }

    //기간 유효성 검사
    public boolean periodCheck() {
        return !end.isBefore(start);
    }

    //종료 여부
    public boolean ended() {
        LocalDate now = LocalDate.now();
        return end != null && now.isAfter(end);
    }
}