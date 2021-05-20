package no.ssb.timeuse.surveyservice.diary;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import no.ssb.timeuse.surveyservice.metrics.CustomMetrics;
import no.ssb.timeuse.surveyservice.metrics.TaggedGauge;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DiaryMetrics {

    private static final String METRICS_PREFIX = "tus.ss.diary.";
    private TaggedGauge tgDbCount;

    private final DiaryRepository diaryRepository;

    public DiaryMetrics(MeterRegistry meterRegistry, DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
        tgDbCount = new TaggedGauge(CustomMetrics.DB_COUNT, "table", meterRegistry);
    }

    public void generateMetrics() {
        countTotals();
    }

    private void countTotals() {
        val totalNumber = diaryRepository.count();
        tgDbCount.set("Diary", totalNumber);
    }
}
