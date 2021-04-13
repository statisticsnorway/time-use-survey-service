package no.ssb.timeuse.surveyservice.respondent;

import java.time.LocalDate;

public interface RespondentMetricsStatusCount {
    LocalDate getDiaryStart();
    String getStatusDiary();
    String getStatusSurvey();
    String getStatusRecruitment();
    String getStatusQuestionnaire();
    Long getTotal();
}
