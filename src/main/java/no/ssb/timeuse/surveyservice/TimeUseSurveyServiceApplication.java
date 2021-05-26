package no.ssb.timeuse.surveyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TimeUseSurveyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimeUseSurveyServiceApplication.class, args);
	}

}
