package no.ssb.timeuse.surveyservice.communicationlog;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Category;
import no.ssb.timeuse.surveyservice.metrics.TaggedGauge;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CommunicationLogMetrics {

    private static final String METRICS_PREFIX = "tus.ss.commlog.";

    private final MeterRegistry meterRegistry;
    private final AtomicInteger gaugeTotal;
    private TaggedGauge taggedGaugeCategory;

    private final CommunicationLogRepository communicationLogRepository;

    public CommunicationLogMetrics(MeterRegistry meterRegistry, CommunicationLogRepository communicationLogRepository) {
        this.communicationLogRepository = communicationLogRepository;
        this.meterRegistry = meterRegistry;

        gaugeTotal = meterRegistry.gauge(METRICS_PREFIX + "total", new AtomicInteger(0));
        taggedGaugeCategory = new TaggedGauge(METRICS_PREFIX+"category", "category", meterRegistry);
    }

    public void generateMetrics() {
        countTotals();
        countPerCategory();
    }

    private void countTotals() {
        val totalNumber = communicationLogRepository.count();
        gaugeTotal.set((int) totalNumber);
    }

    private void countPerCategory() {
        List<Object[]> list = communicationLogRepository.getNumberOfCommLogEntriesPerCategory();
        for (Object[] ob : list) {
            Category category = (Category) ob[0];
            Long count = (Long) ob[1];
            taggedGaugeCategory.set(category.name(), count);
        }
    }

}
