package backend.airo.security.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import java.io.FileInputStream;

@Configuration
@Slf4j
public class FirebaseConfig {
    @Value("${social.firebase.service-account-key}")
    private String serviceAccountKeyPath;

    @Value("${social.firebase.project-id}")
    private String projectId;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                FileInputStream serviceAccount = new FileInputStream(serviceAccountKeyPath);
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setProjectId(projectId)
                        .build();

                FirebaseApp app = FirebaseApp.initializeApp(options);
                log.info("Firebase 초기화 성공: projectId = {}", projectId);
                return app;
            }
            return FirebaseApp.getInstance();
        } catch (IOException e) {
            log.error("Firebase 초기화 실패: {}", e.getMessage());
            throw e;
        }
    }
}