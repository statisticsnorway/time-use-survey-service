package no.ssb.timeuse.surveyservice.interviewer;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import no.ssb.timeuse.surveyservice.metrics.CustomMetrics;
import no.ssb.timeuse.surveyservice.metrics.TaggedGauge;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InterviewerMetrics {

    private static final String METRICS_PREFIX = "tus.ss.interviewer.";
    private TaggedGauge tgDbCount;

    private final InterviewerRepository interviewerRepository;

    public InterviewerMetrics(MeterRegistry meterRegistry, InterviewerRepository interviewerRepository) {
        this.interviewerRepository = interviewerRepository;
        tgDbCount = new TaggedGauge(CustomMetrics.DB_COUNT, "table", meterRegistry);
    }

    public void generateMetrics() {
        countTotals();
    }

    private void countTotals() {
        val totalNumber = interviewerRepository.count();
        tgDbCount.set("Interviewer", totalNumber);
    }
}
