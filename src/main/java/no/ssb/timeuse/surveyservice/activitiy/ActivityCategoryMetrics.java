package no.ssb.timeuse.surveyservice.activitiy;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import no.ssb.timeuse.surveyservice.metrics.CustomMetrics;
import no.ssb.timeuse.surveyservice.metrics.TaggedGauge;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ActivityCategoryMetrics {

    private static final String METRICS_PREFIX = "tus.ss.activitiy.";
    private TaggedGauge tgDbCount;

    private final ActivityCategoryRepository activityCategoryRepository;

    public ActivityCategoryMetrics(MeterRegistry meterRegistry, ActivityCategoryRepository activityCategoryRepository) {
        this.activityCategoryRepository = activityCategoryRepository;
        tgDbCount = new TaggedGauge(CustomMetrics.DB_COUNT, "table", meterRegistry);
    }

    public void generateMetrics() {
        countTotals();
    }

    private void countTotals() {
        val totalNumber = activityCategoryRepository.count();
        tgDbCount.set("Activity", totalNumber);
    }

}
