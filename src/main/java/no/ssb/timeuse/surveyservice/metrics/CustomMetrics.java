package no.ssb.timeuse.surveyservice.metrics;

import lombok.AllArgsConstructor;
import no.ssb.timeuse.surveyservice.activitiy.ActivityCategoryMetrics;
import no.ssb.timeuse.surveyservice.appointment.AppointmentMetrics;
import no.ssb.timeuse.surveyservice.communicationlog.CommunicationLogMetrics;
import no.ssb.timeuse.surveyservice.household.HouseholdMetrics;
//import no.ssb.timeuse.surveyservice.item.ItemMetrics;
//import no.ssb.timeuse.surveyservice.purchase.PurchaseMetrics;
import no.ssb.timeuse.surveyservice.respondent.RespondentMetrics;
import no.ssb.timeuse.surveyservice.searchterm.SearchTermMetrics;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomMetrics {

    private final ActivityCategoryMetrics activityCategoryMetrics;
    private final CommunicationLogMetrics communicationLogMetrics;
    private final HouseholdMetrics householdMetrics;
//    private final ItemMetrics itemMetrics;
//    private final PurchaseMetrics purchaseMetrics;
    private final RespondentMetrics respondentMetrics;
    private final SearchTermMetrics searchTermMetrics;
    private final AppointmentMetrics appointmentMetrics;

    @Scheduled(initialDelay = 20000L, fixedDelay = 60000L)
    void calculateMetrics() {

        activityCategoryMetrics.generateMetrics();
        communicationLogMetrics.generateMetrics();
//        itemMetrics.generateMetrics();
//        purchaseMetrics.generateMetrics();
        searchTermMetrics.generateMetrics();
        appointmentMetrics.generateMetrics();
        householdMetrics.generateMetrics();
        respondentMetrics.generateMetrics();
    }
}
