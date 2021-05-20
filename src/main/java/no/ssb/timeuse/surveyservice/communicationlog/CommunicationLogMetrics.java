package no.ssb.timeuse.surveyservice.communicationlog;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Category;
import no.ssb.timeuse.surveyservice.metrics.CustomMetrics;
import no.ssb.timeuse.surveyservice.metrics.TaggedGauge;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CommunicationLogMetrics {

    private static final String METRICS_PREFIX = "tus.ss.commlog.";

    private TaggedGauge taggedGaugeCategory;
    private TaggedGauge tgDbCount;

    private final CommunicationLogRepository communicationLogRepository;

    public CommunicationLogMetrics(MeterRegistry meterRegistry, CommunicationLogRepository communicationLogRepository) {
        this.communicationLogRepository = communicationLogRepository;

        tgDbCount = new TaggedGauge(CustomMetrics.DB_COUNT, "table", meterRegistry);
        taggedGaugeCategory = new TaggedGauge(METRICS_PREFIX+"category", "category", meterRegistry);
    }

    @Timed(value = "tus.ss.custom.metrics", description = "Time taken to generate metrics")
    public void generateMetrics() {
        countTotals();
        countPerCategory();
    }

    private void countTotals() {
        val totalNumber = communicationLogRepository.count();
        tgDbCount.set("CommLog", totalNumber);
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
