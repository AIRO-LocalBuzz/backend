package backend.airo.api.global.aop;

import backend.airo.api.global.dto.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.lettuce.core.RedisCommandTimeoutException;
import io.lettuce.core.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.conscrypt.ct.SerializationException;
import org.springframework.cache.Cache;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class RedisCacheGlobalExceptionHandler {

    @ExceptionHandler({
            Cache.ValueRetrievalException.class,
            RedisConnectionFailureException.class,
            RedisCommandTimeoutException.class,
            RedisSystemException.class,
            RedisException.class,
            SerializationException.class,
            JsonProcessingException.class,
            JsonMappingException.class,
            ClassCastException.class,
            IllegalArgumentException.class
    })
    public Response<Void> handleRedisCacheExceptions(Exception e) {
        Throwable root = rootCause(e);
        log.error("[CACHE] handled: {}, root: {}", e.getClass().getSimpleName(), root.toString(), e);

        HttpStatus status;
        String code;

        if (root instanceof RedisConnectionFailureException || root instanceof RedisCommandTimeoutException) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
            code = "REDIS_UNAVAILABLE";
        } else if (root instanceof SerializationException
                || root instanceof JsonProcessingException
                || root instanceof JsonMappingException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            code = "REDIS_SERIALIZATION_ERROR";
        } else if (root instanceof ClassCastException || root instanceof IllegalArgumentException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            code = "REDIS_CACHE_TYPE_ERROR";
        } else if (e instanceof Cache.ValueRetrievalException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            code = "REDIS_CACHE_LOAD_FAILED";
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            code = "REDIS_ERROR";
        }

        return Response.error(code, root.getMessage());
    }

    private static Throwable rootCause(Throwable t) {
        Throwable cur = t;
        while (cur.getCause() != null && cur.getCause() != cur) cur = cur.getCause();
        return cur;
    }
}