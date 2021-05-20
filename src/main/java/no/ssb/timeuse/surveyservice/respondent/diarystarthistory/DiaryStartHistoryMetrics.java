package no.ssb.timeuse.surveyservice.respondent.diarystarthistory;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DiaryStartHistoryMetrics {

    private static final String METRICS_PREFIX = "tus.ss.diarystarthist.";
    private final AtomicInteger gaugeTotal;

    private final DiaryStartHistoryRepository diaryStartHistoryRepository;

    public DiaryStartHistoryMetrics(MeterRegistry meterRegistry, DiaryStartHistoryRepository diaryStartHistoryRepository) {
        this.diaryStartHistoryRepository = diaryStartHistoryRepository;
        this.gaugeTotal = meterRegistry.gauge(METRICS_PREFIX + "total", new AtomicInteger(0));    }

    public void generateMetrics() {
        countTotals();
    }

    private void countTotals() {
        val totalNumber = diaryStartHistoryRepository.count();
        gaugeTotal.set((int) totalNumber);
    }

}
