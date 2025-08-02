package backend.airo.common.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class ApplicationServiceConfig implements AsyncConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationServiceConfig.class);

    @PostConstruct
    public void init() {
        logger.info("ApplicationConfig init");
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("EVENT-");
        executor.initialize();
        return executor;
    }
}
