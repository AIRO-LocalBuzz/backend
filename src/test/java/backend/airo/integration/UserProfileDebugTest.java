package backend.airo.integration;

import backend.airo.api.user.dto.CreateUserProfileRequest;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.yml")
class UserProfileDebugTest {

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
    @DisplayName("회원 프로필 생성 디버그 테스트")
    void createUserProfileDebugTest() throws Exception {
        // given
        CreateUserProfileRequest createRequest = new CreateUserProfileRequest(
                "홍길동",
                "010-1234-5678",
                LocalDate.of(1990, 1, 1),
                "hong@example.com"
        );

        // Test the user profile creation endpoint
        mockMvc.perform(post("/v1/user-profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print());
    }
}