package no.ssb.timeuse.surveyservice.metrics;

import lombok.AllArgsConstructor;
import no.ssb.timeuse.surveyservice.household.HouseholdMetrics;
import no.ssb.timeuse.surveyservice.respondent.RespondentMetrics;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomMetrics {

    private final HouseholdMetrics householdMetrics;
    private final RespondentMetrics respondentMetrics;

    @Scheduled(initialDelay = 20000L, fixedDelay = 60000L)
    void calculateMetrics() {

        householdMetrics.generateMetrics();
        respondentMetrics.generateMetrics();
    }
}
