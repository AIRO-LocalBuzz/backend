package backend.airo.domain.point;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class TradePoint{

    private final Long id;
    private final Long useedPoint;
    private final Long userId;
    private final String item_name;
    private final LocalDateTime exchangedAt;

}
