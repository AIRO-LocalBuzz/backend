package backend.airo.domain.image.util;

import backend.airo.common.exception.NullCheckException;
import backend.airo.common.exception.NullErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NullCheckProvider {

    public static <T> T checkNotNull(T object, NullErrorCode errorCode, Class<?> sourceClass) {
        if (object == null) {
            throw new NullCheckException(errorCode, String.format("%s", sourceClass.getSimpleName()));
        }
        return object;
    }

    public static Long checkNotNull(Long id, NullErrorCode errorCode, Class<?> sourceClass) {
        if (id == null) {
            throw new NullCheckException(errorCode, String.format("%s", sourceClass.getSimpleName()));
        }
        return id;
    }

}
