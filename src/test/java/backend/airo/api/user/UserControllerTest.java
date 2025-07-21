package backend.airo.api.user;

import backend.airo.api.user.dto.LoginRequest;
import backend.airo.application.user.usecase.UserUseCase;
import backend.airo.domain.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserUseCase userUseCase;

    @Test
    @DisplayName("POST /v1/auth/login - 성공")
    void login_Success() throws Exception {
        // given
        LoginRequest request = new LoginRequest("valid-firebase-token");
        User mockUser = new User(1L, "firebase-uid-123", "test@example.com", "테스트유저", null, null, null);
        
        when(userUseCase.loginWithFirebase(anyString())).thenReturn(mockUser);

        // when & then
        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.firebaseUid").value("firebase-uid-123"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.nickname").value("테스트유저"));
    }

    @Test
    @DisplayName("GET /v1/auth/me - 성공")
    void getCurrentUser_Success() throws Exception {
        // given
        User mockUser = new User(1L, "firebase-uid-123", "test@example.com", "테스트유저", null, null, null);
        when(userUseCase.getUserById(1L)).thenReturn(mockUser);

        // when & then
        mockMvc.perform(get("/v1/auth/me")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.firebaseUid").value("firebase-uid-123"));
    }

    @Test
    @DisplayName("GET /v1/auth/firebase/{firebaseUid} - 성공")
    void getUserByFirebaseUid_Success() throws Exception {
        // given
        User mockUser = new User(1L, "firebase-uid-123", "test@example.com", "테스트유저", null, null, null);
        when(userUseCase.getUserByFirebaseUid(any())).thenReturn(mockUser);

        // when & then
        mockMvc.perform(get("/v1/auth/firebase/firebase-uid-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.firebaseUid").value("firebase-uid-123"));
    }
}