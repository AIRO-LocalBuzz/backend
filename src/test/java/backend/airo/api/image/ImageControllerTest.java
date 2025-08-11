// java
package backend.airo.api.image;

import backend.airo.api.global.aop.GlobalControllerErrorHandler;
import backend.airo.api.global.config.ValidationErrorMappingConfig;
import backend.airo.api.image.dto.ImageCreateRequest;
import backend.airo.api.image.dto.ImageReorderRequest;
import backend.airo.api.test.support.UserPrincipalArgumentResolver;
import backend.airo.application.image.usecase.ImageCreateUseCase;
import backend.airo.application.image.usecase.ImageDeleteUseCase;
import backend.airo.application.image.usecase.ImageReadUseCase;
import backend.airo.application.image.usecase.ImageUpdateUseCase;
import backend.airo.domain.image.Image;
import backend.airo.domain.image.exception.ImageErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("ImageController 테스트")
class ImageControllerTest {

    @Mock
    private ImageReadUseCase imageReadUseCase;
    @Mock
    private ImageCreateUseCase imageCreateUseCase;
    @Mock
    private ImageUpdateUseCase imageUpdateUseCase;
    @Mock
    private ImageDeleteUseCase imageDeleteUseCase;
    @Mock
    private ValidationErrorMappingConfig errorMappingConfig;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    // java
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        lenient().when(errorMappingConfig.getPathVariableError("imageId"))
                .thenReturn(ImageErrorCode.IMAGE_ID_REQUIRED);

        ImageController imageController = new ImageController(
                imageReadUseCase, imageCreateUseCase,
                imageUpdateUseCase, imageDeleteUseCase
        );
        mockMvc = MockMvcBuilders.standaloneSetup(imageController)
                .setControllerAdvice(new GlobalControllerErrorHandler(errorMappingConfig))
                .setCustomArgumentResolvers(new UserPrincipalArgumentResolver())
                .build();
    }

    @Test
    @DisplayName("정상적인 이미지 ID로 이미지 조회 성공")
    void getImage_ValidImageId_Success() throws Exception {
        Long imageId = 1L;
        Image mockImage = createMockImage(imageId);
        when(imageReadUseCase.getSingleImage(imageId)).thenReturn(mockImage);

        mockMvc.perform(get("/v1/images/{imageId}", imageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(imageId))
                .andExpect(jsonPath("$.imageUrl").value("https://example.com/images/test-image.jpg"));
    }


    @Test
    @DisplayName("문자열 이미지 ID 요청시 400 에러 반환")
    void getImage_StringImageId_ReturnsBadRequest() throws Exception {
        String invalidImageId = "abc";

        mockMvc.perform(get("/v1/images/{imageId}", invalidImageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.responseCode").value("IMAGE_ID_REQUIRED"))
                .andExpect(jsonPath("$.message").value("잘못된 이미지 ID입니다"));
    }

    @Test
    @DisplayName("소수점 이미지 ID 요청시 400 에러 반환")
    void getImage_DecimalImageId_ReturnsBadRequest() throws Exception {
        String decimalImageId = "1.5";

        mockMvc.perform(get("/v1/images/{imageId}", decimalImageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.responseCode").value("IMAGE_ID_REQUIRED"))
                .andExpect(jsonPath("$.message").value("잘못된 이미지 ID입니다"));
    }



    @Test
    @DisplayName("서비스 예외 발생시 500 에러 반환")
    void getImage_ServiceException_ReturnsError() throws Exception {
        Long imageId = 1L;
        when(imageReadUseCase.getSingleImage(anyLong()))
                .thenThrow(new RuntimeException("Internal service error"));

        mockMvc.perform(get("/v1/images/{imageId}", imageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").exists());
    }

    private Image createMockImage(Long imageId) {
        return Image.builder()
                .id(imageId)
                .imageUrl("https://example.com/images/test-image.jpg")
                .mimeType("image/jpeg")
                .build();
    }



    @Test
    public void uploadSingleImage_ValidRequest_ReturnsCreated() throws Exception {
        // 단일 이미지 업로드 요청
        ImageCreateRequest request = new ImageCreateRequest("https://example.com/image.jpg", "image/jpeg");

        Image image = Image.builder()
                .id(1L)
                .imageUrl(request.imageUrl())
                .mimeType(request.mimeType())
                .build();

        Mockito.when(imageCreateUseCase.uploadSingleImage(any()))
                .thenReturn(image);

        mockMvc.perform(post("/v1/images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.imageUrl").value(request.imageUrl()));
    }

    @Test
    public void uploadMultipleImages_ValidRequest_ReturnsCreated() throws Exception {
        // 다중 이미지 업로드 요청
        ImageCreateRequest req1 = new ImageCreateRequest("https://example.com/image1.jpg", "image/jpeg");
        ImageCreateRequest req2 = new ImageCreateRequest("https://example.com/image2.jpg", "image/png");
        List<ImageCreateRequest> requests = List.of(req1, req2);

        Image image1 = Image.builder()
                .id(1L)
                .imageUrl(req1.imageUrl())
                .mimeType(req1.mimeType())
                .build();
        Image image2 = Image.builder()
                .id(2L)
                .imageUrl(req2.imageUrl())
                .mimeType(req2.mimeType())
                .build();

        Mockito.when(imageCreateUseCase.uploadMultipleImages(any()))
                .thenReturn(List.of(image1, image2));

        mockMvc.perform(post("/v1/images/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].imageUrl").value(req1.imageUrl()))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].imageUrl").value(req2.imageUrl()));
    }

    @Test
    public void reorderImages_ValidRequest_ReturnsOk() throws Exception {
        // 이미지 순서 재정렬 요청
        ImageReorderRequest request = new ImageReorderRequest(List.of(2L, 1L));

        Image image1 = Image.builder().id(2L).build();
        Image image2 = Image.builder().id(1L).build();

        Mockito.when(imageUpdateUseCase.reorderImages(any()))
                .thenReturn(List.of(image1, image2));

        mockMvc.perform(put("/v1/images/reorder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[1].id").value(1));
    }


//    @Test
//    @DisplayName("검증 실패 시 AiroException으로 변환되어 에러 응답 반환")
//    void uploadSingleImage_InvalidRequest_TriggersHandleValidationException() throws Exception {
//        // 빈 imageUrl로 검증 실패 케이스 생성
//        ImageCreateRequest invalidRequest = new ImageCreateRequest("", "image/jpeg");
//
//        mockMvc.perform(post("/v1/images")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidRequest)))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                // GlobalControllerErrorHandler에서 변환된 에러 응답 검증
//                .andExpect(jsonPath("$.responseCode").value("VALIDATION_FAILED"))
//                .andExpect(jsonPath("$.message").value("요청 데이터가 유효하지 않습니다."))
//                .andExpect(jsonPath("$.data.imageUrl").value("이미지 URL은 필수 입력값입니다."));
//    }


    // java
    @Test
    @DisplayName("검증 실패 시 기본 에러로 에러 응답 반환")
    void uploadSingleImage_InvalidRequest_ReturnsBadRequest() throws Exception {
        // 빈 imageUrl로 검증 실패 케이스 생성
        ImageCreateRequest invalidRequest = new ImageCreateRequest("", "image/jpeg");

        mockMvc.perform(post("/v1/images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("이미지 URL은 필수입니다"));
    }
}