package backend.airo.api.user;

import backend.airo.api.user.dto.CreateUserProfileRequest;
import backend.airo.api.user.dto.UpdateUserProfileRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.yml")
class UserProfileControllerTest {

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
    @DisplayName("회원 프로필 생성")
    void createUserProfile() throws Exception {
        // given
        CreateUserProfileRequest request = new CreateUserProfileRequest(
                "홍길동",
                "010-1234-5678",
                LocalDate.of(1990, 1, 1),
                "hong@example.com"
        );

        // when & then
        mockMvc.perform(post("/api/v1/user-profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("홍길동"))
                .andExpect(jsonPath("$.data.contact").value("010-1234-5678"))
                .andExpect(jsonPath("$.data.email").value("hong@example.com"))
                .andExpect(jsonPath("$.data.birthDate").value("1990-01-01"));
    }

    @Test
    @DisplayName("회원 프로필 조회")
    void getUserProfile() throws Exception {
        // given - 먼저 회원을 생성
        CreateUserProfileRequest createRequest = new CreateUserProfileRequest(
                "김영희",
                "010-5678-1234",
                LocalDate.of(1985, 5, 15),
                "kim@example.com"
        );

        String response = mockMvc.perform(post("/api/v1/user-profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ID 추출 (간단한 방법)
        Long id = 1L; // H2 DB에서 첫 번째 생성된 ID

        // when & then
        mockMvc.perform(get("/api/v1/user-profiles/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id))
                .andExpect(jsonPath("$.data.name").value("김영희"))
                .andExpect(jsonPath("$.data.contact").value("010-5678-1234"))
                .andExpect(jsonPath("$.data.email").value("kim@example.com"));
    }

    @Test
    @DisplayName("존재하지 않는 회원 프로필 조회 시 404 에러")
    void getUserProfileNotFound() throws Exception {
        // when & then
        mockMvc.perform(get("/api/v1/user-profiles/{id}", 999L))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("회원 프로필 목록 조회")
    void getAllUserProfiles() throws Exception {
        // given - 여러 회원 생성
        CreateUserProfileRequest request1 = new CreateUserProfileRequest(
                "사용자1", "010-1111-1111", LocalDate.of(1990, 1, 1), "user1@example.com");
        CreateUserProfileRequest request2 = new CreateUserProfileRequest(
                "사용자2", "010-2222-2222", LocalDate.of(1992, 2, 2), "user2@example.com");

        mockMvc.perform(post("/api/v1/user-profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/user-profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isOk());

        // when & then
        mockMvc.perform(get("/api/v1/user-profiles"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    @DisplayName("회원 프로필 수정")
    void updateUserProfile() throws Exception {
        // given - 먼저 회원 생성
        CreateUserProfileRequest createRequest = new CreateUserProfileRequest(
                "원본이름",
                "010-0000-0000",
                LocalDate.of(1990, 1, 1),
                "original@example.com"
        );

        mockMvc.perform(post("/api/v1/user-profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk());

        Long id = 1L;

        UpdateUserProfileRequest updateRequest = new UpdateUserProfileRequest(
                "수정된이름",
                "010-9999-9999",
                LocalDate.of(1995, 12, 31),
                "updated@example.com"
        );

        // when & then
        mockMvc.perform(put("/api/v1/user-profiles/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id))
                .andExpect(jsonPath("$.data.name").value("수정된이름"))
                .andExpect(jsonPath("$.data.contact").value("010-9999-9999"))
                .andExpect(jsonPath("$.data.email").value("updated@example.com"));
    }

    @Test
    @DisplayName("회원 프로필 삭제")
    void deleteUserProfile() throws Exception {
        // given - 먼저 회원 생성
        CreateUserProfileRequest createRequest = new CreateUserProfileRequest(
                "삭제될사용자",
                "010-1111-2222",
                LocalDate.of(1988, 8, 8),
                "delete@example.com"
        );

        mockMvc.perform(post("/api/v1/user-profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk());

        Long id = 1L;

        // when & then - 삭제
        mockMvc.perform(delete("/api/v1/user-profiles/{id}", id))
                .andDo(print())
                .andExpect(status().isOk());

        // 삭제 후 조회 시 404 에러가 발생해야 함
        mockMvc.perform(get("/api/v1/user-profiles/{id}", id))
                .andExpect(status().is4xxClientError());
    }
}