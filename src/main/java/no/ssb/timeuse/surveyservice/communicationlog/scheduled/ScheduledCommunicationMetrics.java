package no.ssb.timeuse.surveyservice.communicationlog.scheduled;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Category;
import no.ssb.timeuse.surveyservice.metrics.CustomMetrics;
import no.ssb.timeuse.surveyservice.metrics.TaggedGauge;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledCommunicationMetrics {

    private static final String METRICS_PREFIX = "tus.ss.schedcomm.";

    private TaggedGauge taggedGaugeCategory;
    private TaggedGauge tgDbCount;

    private final ScheduledCommunicationRepository scheduledCommunicationRepository;

    public ScheduledCommunicationMetrics(MeterRegistry meterRegistry,
                                         ScheduledCommunicationRepository scheduledCommunicationRepository) {
        this.scheduledCommunicationRepository = scheduledCommunicationRepository;

        tgDbCount = new TaggedGauge(CustomMetrics.DB_COUNT, "table", meterRegistry);
        taggedGaugeCategory = new TaggedGauge(METRICS_PREFIX+"category", "category", meterRegistry);
    }

    public void generateMetrics() {
        countTotals();
        countPerCategory();
    }

    private void countTotals() {
        val totalNumber = scheduledCommunicationRepository.count();
        tgDbCount.set("CommLog", totalNumber);
    }

    private void countPerCategory() {
        List<Object[]> list = scheduledCommunicationRepository.getNumberOfSchedCommPerCategory();
        for (Object[] ob : list) {
            Category category = (Category) ob[0];
            Long count = (Long) ob[1];
            taggedGaugeCategory.set(category.name(), count);
        }
    }

}
