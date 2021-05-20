package no.ssb.timeuse.surveyservice.templates;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import no.ssb.timeuse.surveyservice.metrics.CustomMetrics;
import no.ssb.timeuse.surveyservice.metrics.TaggedGauge;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TemplateMetrics {

    private static final String METRICS_PREFIX = "tus.ss.template.";
    private TaggedGauge tgDbCount;

    private final TemplateRepository templateRepository;

    public TemplateMetrics(MeterRegistry meterRegistry, TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
        tgDbCount = new TaggedGauge(CustomMetrics.DB_COUNT, "table", meterRegistry);
    }

    public void generateMetrics() {
        countTotals();
    }

    private void countTotals() {
        val totalNumber = templateRepository.count();
        tgDbCount.set("Template", totalNumber);
    }

}
