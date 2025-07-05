package event.reminder.reminder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EventTrackerApplication {
	public static void main(String[] args) {
		SpringApplication.run(EventTrackerApplication.class, args);
	}
}
