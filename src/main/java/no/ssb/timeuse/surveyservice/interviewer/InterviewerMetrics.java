package no.ssb.timeuse.surveyservice.interviewer;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InterviewerMetrics {

    private static final String METRICS_PREFIX = "tus.ss.interviewer.";
    private final AtomicInteger gaugeTotal;

    private final InterviewerRepository interviewerRepository;

    public InterviewerMetrics(MeterRegistry meterRegistry, InterviewerRepository interviewerRepository) {
        this.interviewerRepository = interviewerRepository;
        gaugeTotal = meterRegistry.gauge(METRICS_PREFIX + "total", new AtomicInteger(0));
    }

    public void generateMetrics() {
        countTotals();
    }

    private void countTotals() {
        val totalNumber = interviewerRepository.count();
        gaugeTotal.set((int) totalNumber);
    }
}
