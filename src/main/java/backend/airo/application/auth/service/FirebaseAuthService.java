package backend.airo.application.auth.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseAuthService {

    public FirebaseToken verifyToken(String idToken) {
        try {
            // Firebase ID Token 검증
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            log.info("Firebase 토큰 검증 성공 - UID: {}", decodedToken.getUid());
            return decodedToken;

        } catch (FirebaseAuthException e) {
            log.error("Firebase 토큰 검증 실패", e);
            throw new RuntimeException("유효하지 않은 Firebase 토큰입니다.", e);
        }
    }
}