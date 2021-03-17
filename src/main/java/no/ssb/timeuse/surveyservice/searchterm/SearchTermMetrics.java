package no.ssb.timeuse.surveyservice.searchterm;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SearchTermMetrics {

    private static final String METRICS_PREFIX = "tus.ss.searchterm.";
    private final AtomicInteger gaugeTotal;

    private final SearchTermRepository searchTermRepository;

    public SearchTermMetrics(MeterRegistry meterRegistry, SearchTermRepository searchTermRepository) {
        gaugeTotal = meterRegistry.gauge(METRICS_PREFIX + "total", new AtomicInteger(0));
        this.searchTermRepository = searchTermRepository;
    }

    public void generateMetrics() {
        countTotals();
    }

    private void countTotals() {
        val totalNumber = searchTermRepository.count();
        gaugeTotal.set((int) totalNumber);
    }
}
