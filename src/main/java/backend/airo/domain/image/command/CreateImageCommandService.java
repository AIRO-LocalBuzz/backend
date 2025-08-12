package backend.airo.domain.image.command;

import backend.airo.domain.image.Image;
import backend.airo.domain.image.exception.ImageErrorCode;
import backend.airo.domain.image.exception.InvalidImageException;
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
        validateMimeType(image.getMimeType());
        return imageRepository.save(image);
    }


    public Image handleWithLock(Image image) {
        uploadLock.lock();
        try {
            validateMimeType(image.getMimeType());
            return imageRepository.save(image);
        } finally {
            uploadLock.unlock();
        }
    }


    private void validateMimeType(String mimeType) {

        if (!SUPPORTED_MIME_TYPES.contains(mimeType.toLowerCase())) {
            throw new UnsupportedFormatException(mimeType, SUPPORTED_MIME_TYPES.toArray(new String[0]));
        }
    }

}
