package no.ssb.timeuse.surveyservice.respondent;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import no.ssb.timeuse.surveyservice.metrics.MultiTaggedGauge;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RespondentMetrics {

    private static final String METRICS_PREFIX = "tus.ss.respondent.";
    private final AtomicInteger gaugeTotal;
    private MultiTaggedGauge mtgStatus;
    private MultiTaggedGauge mtgDiaryStartHeatmap;
    private MultiTaggedGauge mtgDiaryEndHeatmap;

    private final RespondentRepository respondentRepository;

    public RespondentMetrics(MeterRegistry meterRegistry, RespondentRepository respondentRepository) {
        this.respondentRepository = respondentRepository;
        gaugeTotal = meterRegistry.gauge(METRICS_PREFIX + "total", new AtomicInteger(0));
        mtgStatus = new MultiTaggedGauge(METRICS_PREFIX + "status", meterRegistry, "date", "diary", "survey", "recruitment", "questionnaire");
        mtgDiaryStartHeatmap = new MultiTaggedGauge(METRICS_PREFIX + "heatmap.diarystart", meterRegistry, "weekday", "survey-status");
        mtgDiaryEndHeatmap = new MultiTaggedGauge(METRICS_PREFIX + "heatmap.diaryend", meterRegistry, "weekday", "survey-status");
    }

    public void generateMetrics() {
        countTotals();
        countPerStatus();
        countPerDiaryStartWeekday();
        countPerDiaryEndWeekday();
    }

    private void countTotals() {
        val totalNumber = respondentRepository.count();
        gaugeTotal.set((int) totalNumber);
    }

    private void countPerStatus() {
        mtgStatus.resetValues();
        List<RespondentMetricsStatusCount> numberOfRespondentsPerStatus = respondentRepository.getNumberOfRespondentsPerStatus();
        for ( RespondentMetricsStatusCount count : numberOfRespondentsPerStatus) {
            String diaryStart = Optional.ofNullable(count.getDiaryStart()).map(String::valueOf).orElse("");
            String statusDiary = Optional.ofNullable(count.getStatusDiary()).map(String::valueOf).orElse("");
            String statusSurvey = Optional.ofNullable(count.getStatusSurvey()).map(String::valueOf).orElse("");
            String statusRecruitment = Optional.ofNullable(count.getStatusRecruitment()).map(String::valueOf).orElse("");
            String statusQuestionnaire = Optional.ofNullable(count.getStatusQuestionnaire()).map(String::valueOf).orElse("");

            mtgStatus.set(count.getTotal(), diaryStart, statusDiary, statusSurvey, statusRecruitment, statusQuestionnaire);
        }
    }

    private void countPerDiaryStartWeekday() {
        mtgDiaryStartHeatmap.resetValues();
        List<RespondentMetricsDayCount> numberOfRespondentsPerDiaryStartWeekday = respondentRepository.getNumberOfRespondentsPerDiaryStartWeekday();
        for ( RespondentMetricsDayCount count : numberOfRespondentsPerDiaryStartWeekday) {
            String weekday = Optional.ofNullable(count.getDayOfWeek()).map(String::valueOf).orElse("");
            String statusSurvey = Optional.ofNullable(count.getStatusSurvey()).map(String::valueOf).orElse("");
            mtgDiaryStartHeatmap.set(count.getTotal(), weekday, statusSurvey);
        }
    }

    private void countPerDiaryEndWeekday() {
        mtgDiaryEndHeatmap.resetValues();
        List<RespondentMetricsDayCount> numberOfRespondentsPerDiaryStartWeekday = respondentRepository.getNumberOfRespondentsPerDiaryEndWeekday();
        for ( RespondentMetricsDayCount count : numberOfRespondentsPerDiaryStartWeekday) {
            String weekday = Optional.ofNullable(count.getDayOfWeek()).map(String::valueOf).orElse("");
            String statusSurvey = Optional.ofNullable(count.getStatusSurvey()).map(String::valueOf).orElse("");
            mtgDiaryEndHeatmap.set(count.getTotal(), weekday, statusSurvey);
        }
    }

}
