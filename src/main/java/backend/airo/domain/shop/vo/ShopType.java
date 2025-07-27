package backend.airo.domain.shop.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ShopType {

    I1("숙박"), I2("음식")
    ;


    private final String typeName;
    // I1 -> 숙박업, I2 -> 음식점업
}
