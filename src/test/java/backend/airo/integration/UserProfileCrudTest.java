package backend.airo.integration;

import backend.airo.api.user.dto.CreateUserProfileRequest;
import backend.airo.api.user.dto.UpdateUserProfileRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.yml")
class UserProfileCrudTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("회원 프로필 전체 CRUD 통합 테스트")
    void userProfileCrudIntegrationTest() throws Exception {
        // 1. CREATE - 회원 프로필 생성
        CreateUserProfileRequest createRequest = new CreateUserProfileRequest(
                "홍길동",
                "010-1234-5678",
                LocalDate.of(1990, 1, 1),
                "hong@example.com"
        );

        MvcResult createResult = mockMvc.perform(post("/v1/user-profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.name").value("홍길동"))
                .andExpect(jsonPath("$.result.contact").value("010-1234-5678"))
                .andExpect(jsonPath("$.result.email").value("hong@example.com"))
                .andReturn();

        // 생성된 ID 추출
        String createResponse = createResult.getResponse().getContentAsString();
        Map<String, Object> responseMap = objectMapper.readValue(createResponse, new TypeReference<Map<String, Object>>() {});
        Map<String, Object> resultMap = (Map<String, Object>) responseMap.get("result");
        Long userId = Long.valueOf(resultMap.get("id").toString());

        assertNotNull(userId);
        assertTrue(userId > 0);

        // 2. READ - 개별 회원 프로필 조회
        mockMvc.perform(get("/v1/user-profiles/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value(userId))
                .andExpect(jsonPath("$.result.name").value("홍길동"))
                .andExpect(jsonPath("$.result.contact").value("010-1234-5678"))
                .andExpect(jsonPath("$.result.email").value("hong@example.com"));

        // 3. READ - 전체 회원 프로필 목록 조회
        mockMvc.perform(get("/v1/user-profiles"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result.length()").value(1))
                .andExpect(jsonPath("$.result[0].id").value(userId));

        // 4. UPDATE - 회원 프로필 수정
        UpdateUserProfileRequest updateRequest = new UpdateUserProfileRequest(
                "홍길동수정",
                "010-9999-8888",
                LocalDate.of(1995, 12, 31),
                "updated@example.com"
        );

        mockMvc.perform(put("/v1/user-profiles/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value(userId))
                .andExpect(jsonPath("$.result.name").value("홍길동수정"))
                .andExpect(jsonPath("$.result.contact").value("010-9999-8888"))
                .andExpect(jsonPath("$.result.email").value("updated@example.com"));

        // 5. DELETE - 회원 프로필 삭제
        mockMvc.perform(delete("/v1/user-profiles/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk());

        // 6. 삭제 후 조회 시 404 에러 확인
        mockMvc.perform(get("/v1/user-profiles/{id}", userId))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        // 7. 전체 목록에서도 삭제된 것 확인
        mockMvc.perform(get("/v1/user-profiles"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result.length()").value(0));
    }

    @Test
    @DisplayName("존재하지 않는 회원 프로필에 대한 에러 처리 테스트")
    void userProfileNotFoundTest() throws Exception {
        // 존재하지 않는 ID로 조회
        mockMvc.perform(get("/v1/user-profiles/{id}", 999L))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        // 존재하지 않는 ID로 수정
        UpdateUserProfileRequest updateRequest = new UpdateUserProfileRequest(
                "테스트",
                "010-1111-2222",
                LocalDate.of(1990, 1, 1),
                "test@example.com"
        );

        mockMvc.perform(put("/v1/user-profiles/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        // 존재하지 않는 ID로 삭제
        mockMvc.perform(delete("/v1/user-profiles/{id}", 999L))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}