package no.ssb.timeuse.surveyservice.communicationlog.contactRespondent;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ContactRespondentSchedulerConfig {

    @Bean
    @ConditionalOnProperty(value = "contact-io.enabled", havingValue = "true")
    public ContactRespondentScheduledTask contactIoScheduledTask() {
        return new ContactRespondentScheduledTask();
    }

}
