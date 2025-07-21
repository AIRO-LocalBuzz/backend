package backend.airo.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
public class FirebaseConfig {

    @Value("${firebase.service-account-key:}")
    private String serviceAccountKey;

    @Value("${firebase.project-id:airo-default}")
    private String projectId;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseOptions options;
            
            if (serviceAccountKey != null && !serviceAccountKey.isEmpty()) {
                // 운영환경: 환경변수에서 서비스 계정 키 로드
                InputStream serviceAccount = new ByteArrayInputStream(
                    serviceAccountKey.getBytes(StandardCharsets.UTF_8)
                );
                GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
                
                options = FirebaseOptions.builder()
                        .setCredentials(credentials)
                        .setProjectId(projectId)
                        .build();
                        
                log.info("Firebase initialized with service account key");
            } else {
                // 개발환경: 기본 자격 증명 사용 (Application Default Credentials)
                options = FirebaseOptions.builder()
                        .setProjectId(projectId)
                        .build();
                        
                log.info("Firebase initialized with default credentials");
            }

            return FirebaseApp.initializeApp(options);
        }
        
        return FirebaseApp.getInstance();
    }
}