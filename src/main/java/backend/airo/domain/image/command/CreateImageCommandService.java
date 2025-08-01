package backend.airo.domain.image.command;

import backend.airo.domain.image.Image;
import backend.airo.domain.image.exception.UnsupportedFormatException;
import backend.airo.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@RequiredArgsConstructor
@Component
public class CreateImageCommandService {

    private final ImageRepository imageRepository;
    private final ReentrantLock uploadLock = new ReentrantLock();

    // 지원하는 MIME 타입들
    private static final List<String> SUPPORTED_MIME_TYPES = Arrays.asList(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/gif"
    );

    public Image handle(Image image) {
        validateImage(image);
        validateMimeType(image.getMimeType());
        return imageRepository.save(image);
    }

    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    public Image handleWithRetry(Image image) {
        validateImage(image);
        validateMimeType(image.getMimeType());

        int maxAttempts = 3;
        int attempt = 0;
        RuntimeException lastException = null;

        while (attempt < maxAttempts) {
            try {
                return imageRepository.save(image);
            } catch (RuntimeException e) {
                lastException = e;
                attempt++;

                // 마지막 시도가 아닌 경우에만 대기
                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(1000 * attempt); // 1초, 2초 대기
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("재시도 중 인터럽트 발생", ie);
                    }
                }
            }
        }

        throw new RuntimeException("최대 재시도 횟수 초과", lastException);
    }

    public Image handleWithLock(Image image) {
        uploadLock.lock();
        try {
            validateImage(image);
            validateMimeType(image.getMimeType());
            return imageRepository.save(image);
        } finally {
            uploadLock.unlock();
        }
    }

    public String generateThumbnail(String originalImageUrl) {
        if (originalImageUrl == null || originalImageUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("원본 이미지 URL은 필수입니다");
        }

        return imageRepository.generateThumbnail(originalImageUrl);
    }

    private void validateImage(Image image) {
        if (image == null) {
            throw new IllegalArgumentException("이미지 정보는 필수입니다");
        }

        if (image.getImageUrl() == null || image.getImageUrl().trim().isEmpty()) {
            throw new IllegalArgumentException("이미지 URL은 필수입니다");
        }
    }

    private void validateMimeType(String mimeType) {
        if (mimeType == null || !SUPPORTED_MIME_TYPES.contains(mimeType.toLowerCase())) {
            throw new UnsupportedFormatException(mimeType, SUPPORTED_MIME_TYPES.toArray(new String[0]));
        }
    }
}
