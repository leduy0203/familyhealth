package familyhealth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FamilyHealthApplication {

	public static void main(String[] args) {
		SpringApplication.run(FamilyHealthApplication.class, args);
	}

}
