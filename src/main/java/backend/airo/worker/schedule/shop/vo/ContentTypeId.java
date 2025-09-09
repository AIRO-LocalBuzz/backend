package backend.airo.worker.schedule.shop.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ContentTypeId {

    FOOD("39"),
    IODGMENT("32")
    ;

    private final String typeId;

}
