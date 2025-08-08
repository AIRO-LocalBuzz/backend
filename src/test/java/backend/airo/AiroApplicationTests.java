package backend.airo;

import backend.airo.application.auth.usecase.GoogleFirebaseLoginUseCase;
import backend.airo.infra.discord.adapter.DiscordAdapter;
import backend.airo.infra.open_api.area_find.client.OpenApiAreaFeignClient;
import backend.airo.infra.open_api.clure_fatvl.client.OpenApiClureFatvlFeignClient;
import backend.airo.infra.open_api.rural_ex.client.OpenApiRuralExFeignClient;
import backend.airo.infra.open_api.shop.client.OpenApiShopFeignClient;
import backend.airo.security.config.FirebaseConfig;
import backend.airo.support.ServerStartupNotifier;
import net.dv8tion.jda.api.JDA;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = {
		"feign.client.config.default.connectTimeout=5000",
		"feign.client.config.default.readTimeout=5000"
})
@MockBean({
		OpenApiClureFatvlFeignClient.class,
		OpenApiAreaFeignClient.class,
		ServerStartupNotifier.class,
		OpenApiRuralExFeignClient.class,
		OpenApiShopFeignClient.class,
		DiscordAdapter.class,
		JDA.class,
		GoogleFirebaseLoginUseCase.class,
		FirebaseConfig.class
})
class AiroApplicationTests {
	@Test
	void contextLoads() {
	}
}