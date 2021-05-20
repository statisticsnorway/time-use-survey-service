package no.ssb.timeuse.surveyservice.respondent.diarystarthistory;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import no.ssb.timeuse.surveyservice.metrics.CustomMetrics;
import no.ssb.timeuse.surveyservice.metrics.TaggedGauge;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DiaryStartHistoryMetrics {

    private static final String METRICS_PREFIX = "tus.ss.diarystarthist.";
    private TaggedGauge tgDbCount;

    private final DiaryStartHistoryRepository diaryStartHistoryRepository;

    public DiaryStartHistoryMetrics(MeterRegistry meterRegistry, DiaryStartHistoryRepository diaryStartHistoryRepository) {
        this.diaryStartHistoryRepository = diaryStartHistoryRepository;
        tgDbCount = new TaggedGauge(CustomMetrics.DB_COUNT, "table", meterRegistry);
    }

    public void generateMetrics() {
        countTotals();
    }

    private void countTotals() {
        val totalNumber = diaryStartHistoryRepository.count();
            tgDbCount.set("DiaryStartHistory", totalNumber);
    }

}
