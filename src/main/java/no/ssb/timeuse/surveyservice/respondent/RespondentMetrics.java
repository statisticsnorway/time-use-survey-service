package no.ssb.timeuse.surveyservice.respondent;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import no.ssb.timeuse.surveyservice.codelist.CodeList;
import no.ssb.timeuse.surveyservice.metrics.MultiTaggedGauge;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class RespondentMetrics {

    private static final String METRICS_PREFIX = "tus.ss.respondent.";
    private final Map<String, String> regionCodes = new HashMap<>();

    private final MeterRegistry meterRegistry;
    private final RespondentRepository respondentRepository;

    private final AtomicInteger gaugeTotal;
    private MultiTaggedGauge mtgStatusSurvey;
    private MultiTaggedGauge mtgStatusRecruitment;
    private MultiTaggedGauge mtgStatusDiary;
    private MultiTaggedGauge mtgStatusQuestionnaire;
    private MultiTaggedGauge mtgDiaryStart;
    private MultiTaggedGauge mtgDiaryStartHeatmap;
    private MultiTaggedGauge mtgDiaryEndHeatmap;


    public RespondentMetrics(MeterRegistry meterRegistry, RespondentRepository respondentRepository) {
        this.respondentRepository = respondentRepository;
        this.meterRegistry = meterRegistry;
        gaugeTotal = meterRegistry.gauge(METRICS_PREFIX + "total", new AtomicInteger(0));
        mtgStatusSurvey = new MultiTaggedGauge(METRICS_PREFIX+"status.survey", meterRegistry, "status", "diary-start", "region");
        mtgStatusRecruitment = new MultiTaggedGauge(METRICS_PREFIX+"status.recruitment", meterRegistry, "status", "diary-start", "region");
        mtgStatusDiary = new MultiTaggedGauge(METRICS_PREFIX+"status.diary", meterRegistry, "status", "diary-start", "region");
        mtgStatusQuestionnaire = new MultiTaggedGauge(METRICS_PREFIX+"status.questionnaire", meterRegistry, "status", "diary-start", "region");
        mtgDiaryStart = new MultiTaggedGauge(METRICS_PREFIX+"status.diaryStart", meterRegistry, "status", "diaryStart", "region");
        mtgDiaryStartHeatmap = new MultiTaggedGauge(METRICS_PREFIX + "heatmap.diarystart", meterRegistry, "weekday", "survey-status");
        mtgDiaryEndHeatmap = new MultiTaggedGauge(METRICS_PREFIX + "heatmap.diaryend", meterRegistry, "weekday", "survey-status");
    }

    public void generateMetrics() {
        countTotals();
        countPerStatusSurvey();
        countPerStatusRecruitment();
        countPerStatusDiary();
        countPerStatusQuestionnaire();
        countPerDiaryStartWeekday();
        countPerDiaryEndWeekday();
    }

    private void countTotals() {
        val totalNumber = respondentRepository.count();
        gaugeTotal.set((int) totalNumber);
    }

    private void countPerStatusSurvey() {
        mtgStatusSurvey.resetValues();
        List<MetricsCountStatusByDiaryStart> numberOfRespondentsPerStatusSurvey = respondentRepository.getNumberOfRespondentsPerStatusSurvey();
        mapMetricsCountStatusByDiaryStartToMultiTaggedGauge(numberOfRespondentsPerStatusSurvey, mtgStatusSurvey, false);
    }

    private void countPerStatusRecruitment() {
        mtgStatusRecruitment.resetValues();
        List<MetricsCountStatusByDiaryStart> numberOfRespondentsPerStatusRecruitment = respondentRepository.getNumberOfRespondentsPerStatusRecruitment();
        mapMetricsCountStatusByDiaryStartToMultiTaggedGauge(numberOfRespondentsPerStatusRecruitment, mtgStatusRecruitment, true);
    }

    private void countPerStatusDiary() {
        mtgStatusDiary.resetValues();
        List<MetricsCountStatusByDiaryStart> numberOfRespondentsPerStatusDiary = respondentRepository.getNumberOfRespondentsPerStatusDiary();
        mapMetricsCountStatusByDiaryStartToMultiTaggedGauge(numberOfRespondentsPerStatusDiary, mtgStatusDiary, false);
    }

    private void countPerStatusQuestionnaire() {
        mtgStatusQuestionnaire.resetValues();
        List<MetricsCountStatusByDiaryStart> numberOfRespondentsPerStatusQuestionnaire = respondentRepository.getNumberOfRespondentsPerStatusQuestionnaire();
        mapMetricsCountStatusByDiaryStartToMultiTaggedGauge(numberOfRespondentsPerStatusQuestionnaire, mtgStatusQuestionnaire, false);
    }

    private void mapMetricsCountStatusByDiaryStartToMultiTaggedGauge(List<MetricsCountStatusByDiaryStart> list, MultiTaggedGauge mtg, boolean includeKeyInStatus) {
        for (MetricsCountStatusByDiaryStart count : list) {
            String status = Optional.ofNullable(CodeList.status.get(count.getStatus())).map(e -> e.getValue()).orElse("");
            if (includeKeyInStatus && !status.isEmpty() && count.getStatus().length()==2) {
                status = count.getStatus() + " " + status;
            }

            String diaryStart = Optional.ofNullable(count.getDiaryStart()).map(String::valueOf).orElse("");
            String region = Optional.ofNullable(regionCodes.get(count.getRegion())).map(String::valueOf).orElse("Uoppgitt");

            mtg.set(count.getTotal(), status, diaryStart, region);
        }
    }


    private void countPerDiaryStartWeekday() {
        mtgDiaryStartHeatmap.resetValues();
        List<RespondentMetricsDayCount> numberOfRespondentsPerDiaryStartWeekday = respondentRepository.getNumberOfRespondentsPerDiaryStartWeekday();
        mapMetricsCountPerDiaryWeekdayToMultiTaggedGauge(numberOfRespondentsPerDiaryStartWeekday, mtgDiaryStartHeatmap);
    }

    private void countPerDiaryEndWeekday() {
        mtgDiaryEndHeatmap.resetValues();
        List<RespondentMetricsDayCount> numberOfRespondentsPerDiaryEndWeekday = respondentRepository.getNumberOfRespondentsPerDiaryEndWeekday();
        mapMetricsCountPerDiaryWeekdayToMultiTaggedGauge(numberOfRespondentsPerDiaryEndWeekday, mtgDiaryEndHeatmap);
    }

    private void mapMetricsCountPerDiaryWeekdayToMultiTaggedGauge(List<RespondentMetricsDayCount> list, MultiTaggedGauge mtg) {
        for ( RespondentMetricsDayCount count : list) {
            String weekday = Optional.ofNullable(count.getDayOfWeek()).map(String::valueOf).orElse("");
            String statusSurvey = Optional.ofNullable(CodeList.status.get(count.getStatusSurvey())).map(e -> e.getValue()).orElse("");
            if (!statusSurvey.isEmpty() && count.getStatusSurvey().length()==2) {
                statusSurvey = count.getStatusSurvey() + " " + statusSurvey;
            }
            mtg.set(count.getTotal(), weekday, statusSurvey);
        }
    }

    private void initCodeLists() {
        // TODO: Get this from KLASS
        regionCodes.put("1", "Oslo og Viken");
        regionCodes.put("2", "Innlandet");
        regionCodes.put("3", "Agder og Sør-Østlandet");
        regionCodes.put("4", "Vestlandet");
        regionCodes.put("5", "Trøndelag");
        regionCodes.put("6", "Nord-Norge");
        regionCodes.put("9", "Uoppgitt");
    }


}
