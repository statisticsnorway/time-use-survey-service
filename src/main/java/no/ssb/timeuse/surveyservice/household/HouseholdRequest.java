package no.ssb.timeuse.surveyservice.household;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private LocalDate diaryStart;
    private LocalDate diaryEnd;
    private String address;
    private String postcode;
    private String city;
    private LocalDateTime recruitmentStart;
    private LocalDateTime recruitmentEnd;
    private Integer recruitmentMinutesSpent;
    private Boolean acceptedInitialDiaryStart;
    private Integer householdSize;
    private String region;
    private String municipalityNumber;
    private String dwellingNumber;
    private String householdType;
}
