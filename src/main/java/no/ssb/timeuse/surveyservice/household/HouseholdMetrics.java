package no.ssb.timeuse.surveyservice.household;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import no.ssb.timeuse.surveyservice.codelist.CodeList;
import no.ssb.timeuse.surveyservice.metrics.TaggedGauge;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class HouseholdMetrics {

    private static final String METRICS_PREFIX = "fbu.ss.household.";

    private final MeterRegistry meterRegistry;
    private final HouseholdRepository householdRepository;

    private final AtomicInteger gaugeTotal;
    private TaggedGauge taggedGaugeStatuses;
    private TaggedGauge taggedGaugeRandom;

    public HouseholdMetrics(MeterRegistry meterRegistry, HouseholdRepository householdRepository) {
        this.householdRepository = householdRepository;
        this.meterRegistry = meterRegistry;

        gaugeTotal = meterRegistry.gauge(METRICS_PREFIX + "total", new AtomicInteger(0));
        taggedGaugeStatuses = new TaggedGauge(METRICS_PREFIX+"statuses", "code", meterRegistry);
        taggedGaugeRandom = new TaggedGauge(METRICS_PREFIX+"random", "code", meterRegistry);
    }

    public void generateMetrics() {
        countTotals();
        countPerStatus();
        randomGauge();
    }

    private void countTotals() {
        val totalNumber = householdRepository.count();
        gaugeTotal.set((int) totalNumber);
    }

    private void countPerStatus() {
        List<Object[]> list = householdRepository.getNumberOfHousehouldsPerStatusSurvey();
        for (Object[] ob : list) {
            String code = (String) ob[0];
            Long count = (Long) ob[1];
            taggedGaugeStatuses.set(CodeList.status.get(code).getValue(), count);
        }
    }

    private void randomGauge() {
        Random rand = new Random();
        int randNumber = rand.nextInt(10000);
        taggedGaugeRandom.set("AA", randNumber);
    }

}
