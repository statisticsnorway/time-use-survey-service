package no.ssb.timeuse.surveyservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Bean
    public WebClient initializeWebClient() {
        return WebClient.builder()
                .build();
    }
}
