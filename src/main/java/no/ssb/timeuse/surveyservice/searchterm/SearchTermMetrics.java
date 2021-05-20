package no.ssb.timeuse.surveyservice.searchterm;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import no.ssb.timeuse.surveyservice.metrics.CustomMetrics;
import no.ssb.timeuse.surveyservice.metrics.TaggedGauge;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SearchTermMetrics {

    private static final String METRICS_PREFIX = "tus.ss.searchterm.";
    private TaggedGauge tgDbCount;

    private final SearchTermRepository searchTermRepository;

    public SearchTermMetrics(MeterRegistry meterRegistry, SearchTermRepository searchTermRepository) {
        this.searchTermRepository = searchTermRepository;
        tgDbCount = new TaggedGauge(CustomMetrics.DB_COUNT, "table", meterRegistry);
    }

    public void generateMetrics() {
        countTotals();
    }

    private void countTotals() {
        val totalNumber = searchTermRepository.count();
        tgDbCount.set("SearchTerm", totalNumber);
    }
}
