package no.ssb.timeuse.surveyservice.respondent;

import java.time.LocalDate;

public interface MetricsCountStatusByDiaryStart {
    LocalDate getDiaryStart();
    String getStatus();
    String getRegion();
    Long getTotal();
}
