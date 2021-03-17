package no.ssb.timeuse.surveyservice.household;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import no.ssb.timeuse.surveyservice.codelist.CodeList;
import no.ssb.timeuse.surveyservice.metrics.MultiTaggedGauge;
import no.ssb.timeuse.surveyservice.metrics.TaggedGauge;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
    private MultiTaggedGauge multiTaggedGaugeTotal;


    public HouseholdMetrics(MeterRegistry meterRegistry, HouseholdRepository householdRepository) {
        this.householdRepository = householdRepository;
        this.meterRegistry = meterRegistry;

        gaugeTotal = meterRegistry.gauge(METRICS_PREFIX + "total", new AtomicInteger(0));
        taggedGaugeStatuses = new TaggedGauge(METRICS_PREFIX+"statuses", "code", meterRegistry);
        taggedGaugeRandom = new TaggedGauge(METRICS_PREFIX+"random", "code", meterRegistry);

        multiTaggedGaugeTotal = new MultiTaggedGauge(METRICS_PREFIX+"groups", meterRegistry, "survey-status", "diary-start", "region", "hh-type", "hh-size");
    }

    public void generateMetrics() {
        countTotals();
        countPerStatus();
        countPerAlot();
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

    private void countPerAlot() {
        List<Object[]> list = householdRepository.getNumberOfHousehouldsPerAlot();
        for (Object[] ob : list) {
            String diaryStart = "";
            String surveyStatus = (String) ob[0];
            if (ob[1] != null) {
                diaryStart = ((LocalDate) ob[1]).toString();
            }
            String region = (String) ob[2];
            String householdType = (String) ob[3];
            Integer householdSize = (Integer) ob[4];
            Long count = (Long) ob[5];

            multiTaggedGaugeTotal.set(count, surveyStatus, diaryStart, region, householdType, householdSize.toString());
        }
    }

    private void randomGauge() {
        Random rand = new Random();
        int randNumber = rand.nextInt(10000);
        taggedGaugeRandom.set("AA", randNumber);
    }

}
