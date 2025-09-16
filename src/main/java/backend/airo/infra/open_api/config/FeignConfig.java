package backend.airo.infra.open_api.config;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.type.LogicalType;
import feign.codec.Decoder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.beans.factory.ObjectFactory;


@Configuration
@EnableFeignClients(
        basePackages = "backend.airo.infra.open_api",
        defaultConfiguration = FeignConfig.FeignJacksonConfig.class
)
public class FeignConfig {

    @Configuration(proxyBeanMethods = false)
    public static class FeignJacksonConfig {
        @Bean
        public ObjectMapper feignObjectMapper() {
            ObjectMapper m = new ObjectMapper();
            m.registerModule(new JavaTimeModule());
            m.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            // 빈 문자열 대응: "" -> null
            m.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            m.coercionConfigFor(LogicalType.POJO)
                    .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);
            // item이 단일 객체로 올 때 배열로 허용
            m.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            return m;
        }
    }

    @Bean
    public Decoder feignDecoder(ObjectMapper feignObjectMapper) {
        var jackson = new MappingJackson2HttpMessageConverter(feignObjectMapper);
        ObjectFactory<HttpMessageConverters> factory =
                () -> new HttpMessageConverters(jackson);
        return new ResponseEntityDecoder(new SpringDecoder(factory));
    }

}
