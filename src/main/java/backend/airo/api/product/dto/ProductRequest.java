package backend.airo.api.product.dto;

import jakarta.validation.constraints.NotBlank;

public record ProductRequest(

        @NotBlank(message = "아이템 이름은 필수 입니다.")
        String itemName,

        @NotBlank(message = "상품 이미지는 필수 입니다.")
        String itemURL,

        @NotBlank(message = "가격은 필수 입니다. ")
        Long itemPrice,

        String itemDescription
){
    public String itemDescriptionOrEmpty() {
        return itemDescription == null ? "" : itemDescription;
    }
}
