package no.ssb.timeuse.surveyservice.templates;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TemplateMetrics {

    private static final String METRICS_PREFIX = "tus.ss.template.";
    private final AtomicInteger gaugeTotal;

    private final TemplateRepository templateRepository;

    public TemplateMetrics(MeterRegistry meterRegistry, TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
        this.gaugeTotal = meterRegistry.gauge(METRICS_PREFIX + "total", new AtomicInteger(0));    }

    public void generateMetrics() {
        countTotals();
    }

    private void countTotals() {
        val totalNumber = templateRepository.count();
        gaugeTotal.set((int) totalNumber);
    }

}
