package backend.airo.api.auth;

import backend.airo.api.auth.dto.SocialLoginRequest;
import backend.airo.application.auth.service.SocialLoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SocialLoginService socialLoginService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void socialLoginEndpointExists() throws Exception {
        SocialLoginRequest request = new SocialLoginRequest("test-token", "google");

        mockMvc.perform(post("/auth/social-login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized()); // We expect 401 since token is invalid
    }

    @Test
    void googleLoginEndpointExists() throws Exception {
        mockMvc.perform(post("/auth/google")
                .param("token", "test-token"))
                .andExpect(status().isUnauthorized()); // We expect 401 since token is invalid
    }

    @Test
    void kakaoLoginEndpointExists() throws Exception {
        mockMvc.perform(post("/auth/kakao")
                .param("token", "test-token"))
                .andExpect(status().isUnauthorized()); // We expect 401 since token is invalid
    }
}