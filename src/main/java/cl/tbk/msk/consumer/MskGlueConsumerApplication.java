package cl.tbk.msk.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MskGlueConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MskGlueConsumerApplication.class, args);
	}
	
    @GetMapping("/health")
    public String getMessages() {
        return "200!";
    }

}
