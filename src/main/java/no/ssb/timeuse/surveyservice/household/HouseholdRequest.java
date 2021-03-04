package no.ssb.timeuse.surveyservice.household;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HouseholdRequest {
    private Integer referenceWeek;
    private UUID mainRespondentId;
    private Long ioNumber;
    private String statusSurvey;
    private String statusRecruitment;
    private String statusDiary;
    private String statusQuestionnaire;
    private String diaryStart;
    private String diaryEnd;
    private String address;
    private String postcode;
    private String city;
    private String recruitmentStart;
    private String recruitmentEnd;
    private Integer recruitmentMinutesSpent;
    private Boolean acceptedInitialDiaryStart;
    private Integer householdSize;

}
