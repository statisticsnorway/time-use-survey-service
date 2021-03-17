package no.ssb.timeuse.surveyservice.activitiy;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ActivityCategoryMetrics {

    private static final String METRICS_PREFIX = "tus.ss.activitiy.";
    private final AtomicInteger gaugeTotal;

    private final ActivityCategoryRepository activityCategoryRepository;

    public ActivityCategoryMetrics(MeterRegistry meterRegistry, ActivityCategoryRepository activityCategoryRepository) {
        this.activityCategoryRepository = activityCategoryRepository;
        gaugeTotal = meterRegistry.gauge(METRICS_PREFIX + "total", new AtomicInteger(0));
    }

    public void generateMetrics() {
        countTotals();
    }

    private void countTotals() {
        val totalNumber = activityCategoryRepository.count();
        gaugeTotal.set((int) totalNumber);
    }

}
