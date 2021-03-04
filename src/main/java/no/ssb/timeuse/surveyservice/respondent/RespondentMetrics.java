package no.ssb.timeuse.surveyservice.respondent;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RespondentMetrics {

    private static final String METRICS_PREFIX = "fbu.ss.respondent.";
    private final AtomicInteger gaugeTotal;

    private final RespondentRepository respondentRepository;

    public RespondentMetrics(MeterRegistry meterRegistry, RespondentRepository respondentRepository) {
        this.respondentRepository = respondentRepository;
        gaugeTotal = meterRegistry.gauge(METRICS_PREFIX + "total", new AtomicInteger(0));
    }

    public void generateMetrics() {
        countTotals();
    }

    private void countTotals() {
        val totalNumber = respondentRepository.count();
        gaugeTotal.set((int) totalNumber);
    }
}
