package backend.airo.application.auth.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
@Slf4j
public class FirebaseAuthService {

    @Value("${social.firebase.service-account-key}")
    private String serviceAccountKey;

    @Value("${social.firebase.project-id}")
    private String projectId;

    @PostConstruct
    public void initialize() {
        if (serviceAccountKey == null || serviceAccountKey.isEmpty()) {
            log.warn("Firebase service account key not configured. Google login will not work.");
            return;
        }

        try {
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new ByteArrayInputStream(serviceAccountKey.getBytes()));

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .setProjectId(projectId)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            log.info("Firebase initialized successfully");
        } catch (IOException e) {
            log.error("Failed to initialize Firebase", e);
        }
    }

    public FirebaseToken verifyToken(String idToken) throws FirebaseAuthException {
        if (FirebaseApp.getApps().isEmpty()) {
            throw new IllegalStateException("Firebase not initialized");
        }
        
        return FirebaseAuth.getInstance().verifyIdToken(idToken);
    }
}