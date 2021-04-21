package no.ssb.timeuse.surveyservice.diary;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DiaryMetrics {

    private static final String METRICS_PREFIX = "tus.ss.diary.";
    private final AtomicInteger gaugeTotal;

    private final DiaryRepository diaryRepository;

    public DiaryMetrics(MeterRegistry meterRegistry, DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
        gaugeTotal = meterRegistry.gauge(METRICS_PREFIX + "total", new AtomicInteger(0));
    }

    public void generateMetrics() {
        countTotals();
    }

    private void countTotals() {
        val totalNumber = diaryRepository.count();
        gaugeTotal.set((int) totalNumber);
    }
}
