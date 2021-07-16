package no.ssb.timeuse.surveyservice.respondent;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import no.ssb.timeuse.surveyservice.codelist.CodeInfo;
import no.ssb.timeuse.surveyservice.codelist.CodeList;
import no.ssb.timeuse.surveyservice.metrics.CustomMetrics;
import no.ssb.timeuse.surveyservice.metrics.MultiTaggedGauge;
import no.ssb.timeuse.surveyservice.metrics.TaggedGauge;
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

    private final RespondentRepository respondentRepository;

    private TaggedGauge tgDbCount;
    private MultiTaggedGauge mtgStatusSurvey;
    private MultiTaggedGauge mtgStatusRecruitment;
    private MultiTaggedGauge mtgStatusDiary;
    private MultiTaggedGauge mtgStatusQuestionnaire;
    private MultiTaggedGauge mtgDiaryStartHeatmap;
    private MultiTaggedGauge mtgDiaryEndHeatmap;


    public RespondentMetrics(MeterRegistry meterRegistry, RespondentRepository respondentRepository) {
        initCodeLists();
        this.respondentRepository = respondentRepository;

        tgDbCount = new TaggedGauge(CustomMetrics.DB_COUNT, "table", meterRegistry);
        mtgStatusSurvey = new MultiTaggedGauge(METRICS_PREFIX+"status.survey", meterRegistry, "status", "diary-start", "region");
        mtgStatusRecruitment = new MultiTaggedGauge(METRICS_PREFIX+"status.recruitment", meterRegistry, "status", "diary-start", "region");
        mtgStatusDiary = new MultiTaggedGauge(METRICS_PREFIX+"status.diary", meterRegistry, "status", "diary-start", "region");
        mtgStatusQuestionnaire = new MultiTaggedGauge(METRICS_PREFIX+"status.questionnaire", meterRegistry, "status", "diary-start", "region");
        mtgDiaryStartHeatmap = new MultiTaggedGauge(METRICS_PREFIX + "heatmap.diarystart", meterRegistry, "year", "month", "weekday", "survey-status");
        mtgDiaryEndHeatmap = new MultiTaggedGauge(METRICS_PREFIX + "heatmap.diaryend", meterRegistry, "year", "month", "weekday", "survey-status");
    }

    @Timed(value = CustomMetrics.TIMED_NAME, description = CustomMetrics.TIMED_DESCRIPTION)
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
        tgDbCount.set("Respondent", totalNumber);
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
            String status = Optional.ofNullable(CodeList.status.get(count.getStatus())).map(CodeInfo::getValue).orElse("");
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

    private void mapMetricsCountPerDiaryWeekdayToMultiTaggedGauge(List<RespondentMetricsDayCount> numberOfRespondentsPerDiaryStartWeekday, MultiTaggedGauge mtgDiaryStartHeatmap) {
        for ( RespondentMetricsDayCount count : numberOfRespondentsPerDiaryStartWeekday) {
            String year = Optional.ofNullable(count.getYear()).map(String::valueOf).orElse("");
            String month = Optional.ofNullable(count.getMonth()).map(String::valueOf).orElse("");
            String weekday = Optional.ofNullable(count.getDayOfWeek()).map(String::valueOf).orElse("");
            String statusSurvey = Optional.ofNullable(count.getStatusSurvey()).map(String::valueOf).orElse("");
            mtgDiaryStartHeatmap.set(count.getTotal(), year, month, weekday, statusSurvey);
        }
    }

    private void countPerDiaryEndWeekday() {
        mtgDiaryEndHeatmap.resetValues();
        List<RespondentMetricsDayCount> numberOfRespondentsPerDiaryStartWeekday = respondentRepository.getNumberOfRespondentsPerDiaryEndWeekday();
        mapMetricsCountPerDiaryWeekdayToMultiTaggedGauge(numberOfRespondentsPerDiaryStartWeekday, mtgDiaryEndHeatmap);
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
