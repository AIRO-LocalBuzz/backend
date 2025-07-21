package backend.airo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AiroApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiroApplication.class, args);
	}

}
