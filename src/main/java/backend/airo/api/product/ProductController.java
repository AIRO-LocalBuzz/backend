package backend.airo.api.product;

import backend.airo.api.global.dto.Response;
import backend.airo.api.product.dto.ProductRequest;
import backend.airo.api.product.dto.ProductResponse;
import backend.airo.application.product.usecase.ProductUseCase;
import backend.airo.domain.Product.Product;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Product 관련 API")
public class ProductController {

    private final ProductUseCase productUseCase;

    @PostMapping("/product")
    public Response<ProductResponse> saveShop(@RequestBody ProductRequest productRequest) {
        Product saveProductItem = productUseCase.saveProduct(productRequest.itemName(), productRequest.itemURL(), productRequest.itemPrice(), productRequest.itemDescriptionOrEmpty());
        return Response.success(ProductResponse.from(saveProductItem));
    }

    @PatchMapping("/product")
    public Response<ProductResponse> updateShopInfo(
            @RequestParam Long shopId,
            @RequestBody ProductRequest productRequest) {
        Product saveProductItem = productUseCase.updateProduct(productRequest.itemName(), productRequest.itemURL(), productRequest.itemPrice(), productRequest.itemDescriptionOrEmpty(), shopId);
        return Response.success(ProductResponse.from(saveProductItem));
    }

    @GetMapping("/product")
    public Response<List<ProductResponse>> getShopList() {
        List<Product> getProductItemList = productUseCase.getProductList();
        return Response.success(getProductItemList.stream().map(ProductResponse::from).toList());
    }

    @GetMapping("/product/Info")
    public Response<ProductResponse> getShopInfo(@RequestParam Long shopId) {
        Product getProductInfo = productUseCase.getProductInfo(shopId);
        return Response.success(ProductResponse.from(getProductInfo));
    }

    @DeleteMapping("/product")
    public Response<Void> deleteShop(@RequestParam Long shopId) {
        productUseCase.deleteProduct(shopId);
        return Response.success();
    }






}
