package backend.airo.support.cache.config;

import backend.airo.support.cache.MeteredCacheManager;
import backend.airo.support.cache.local.CacheName;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@EnableCaching
@Configuration
@RequiredArgsConstructor
public class RedisCacheConfig {

    private final RedisSerializerConfig redisSerializerConfig;

    @Bean("redisCacheManager")
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(redisCacheConfiguration(Duration.ofMinutes(1)))
                .withInitialCacheConfigurations(customConfigurationMap())
                .build();
    }

    @Bean @Primary
    public CacheManager cacheManager(RedisCacheManager redis, MeterRegistry registry) {
        return new MeteredCacheManager(redis, registry);
    }

    private RedisCacheConfiguration redisCacheConfiguration(Duration ttl) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .computePrefixWith(cacheName -> "airo:" + cacheName + "::")
                .entryTtl(ttl)
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializerConfig.keySerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializerConfig.valueSerializer()));
    }

    private Map<String, RedisCacheConfiguration> customConfigurationMap() {
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        for (CacheName cacheName : CacheName.values()) {
            configMap.put(cacheName.name(), redisCacheConfiguration(cacheName.getDuration()));
        }
        return configMap;
    }

}
