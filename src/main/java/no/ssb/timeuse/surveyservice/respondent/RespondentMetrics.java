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

    private final RespondentRepository respondentRepository;

    public RespondentMetrics(MeterRegistry meterRegistry, RespondentRepository respondentRepository) {
        this.respondentRepository = respondentRepository;
        gaugeTotal = meterRegistry.gauge(METRICS_PREFIX + "total", new AtomicInteger(0));
        mtgStatus = new MultiTaggedGauge(METRICS_PREFIX + "status", meterRegistry, "date", "diary", "survey", "recruitment", "questionnaire");
    }

    public void generateMetrics() {
        countTotals();
        countPerStatus();
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
}
