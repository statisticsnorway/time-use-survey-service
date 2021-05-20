package no.ssb.timeuse.surveyservice.metrics;

import lombok.AllArgsConstructor;
import no.ssb.timeuse.surveyservice.activitiy.ActivityCategoryMetrics;
import no.ssb.timeuse.surveyservice.appointment.AppointmentMetrics;
import no.ssb.timeuse.surveyservice.communicationlog.CommunicationLogMetrics;
//import no.ssb.timeuse.surveyservice.item.ItemMetrics;
//import no.ssb.timeuse.surveyservice.purchase.PurchaseMetrics;
import no.ssb.timeuse.surveyservice.diary.DiaryMetrics;
import no.ssb.timeuse.surveyservice.interviewer.InterviewerMetrics;
import no.ssb.timeuse.surveyservice.respondent.RespondentMetrics;
import no.ssb.timeuse.surveyservice.respondent.diarystarthistory.DiaryStartHistoryMetrics;
import no.ssb.timeuse.surveyservice.searchterm.SearchTermMetrics;
import no.ssb.timeuse.surveyservice.templates.TemplateMetrics;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomMetrics {

    public static final String DB_COUNT = "tus.ss.db.count";

    private final ActivityCategoryMetrics activityCategoryMetrics;
    private final CommunicationLogMetrics communicationLogMetrics;
    private final RespondentMetrics respondentMetrics;
    private final SearchTermMetrics searchTermMetrics;
    private final AppointmentMetrics appointmentMetrics;
    private final TemplateMetrics templateMetrics;
    private final DiaryMetrics diaryMetrics;
    private final InterviewerMetrics interviewerMetrics;
    private final DiaryStartHistoryMetrics diaryStartHistoryMetrics;

    @Scheduled(initialDelay = 20000L, fixedDelay = 60000L)
    void calculateMetrics() {

        activityCategoryMetrics.generateMetrics();
        communicationLogMetrics.generateMetrics();
        searchTermMetrics.generateMetrics();
        appointmentMetrics.generateMetrics();
        respondentMetrics.generateMetrics();
        templateMetrics.generateMetrics();
        diaryMetrics.generateMetrics();
        interviewerMetrics.generateMetrics();
        diaryStartHistoryMetrics.generateMetrics();
    }
}
