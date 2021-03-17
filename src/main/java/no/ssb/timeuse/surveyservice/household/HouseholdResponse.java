package no.ssb.timeuse.surveyservice.household;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.ssb.timeuse.surveyservice.respondent.RespondentResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HouseholdResponse {
    Long id;
    Integer referenceWeek;
    UUID mainRespondentId;
    Long ioNumber;
    String statusSurvey;
    String statusRecruitment;
    String statusDiary;
    String statusQuestionnaire;
    LocalDate diaryStart;
    LocalDate diaryEnd;
    String address;
    String postcode;
    String city;
    LocalDateTime recruitmentStart;
    LocalDateTime recruitmentEnd;
    Integer recruitmentMinutesSpent;
    Boolean acceptedInitialDiaryStart;
    Integer householdSize;
    String region;
    String municipalityNumber;
    String dwellingNumber;
    String householdType;

    List<RespondentResponse> respondentList;

    public static HouseholdResponse map(Household from) {
        return new HouseholdResponse(
                from.getId(),
                from.getReferenceWeek(),
                from.getMainRespondentId(),
                from.getIoNumber(),
                from.getStatusSurvey(),
                from.getStatusRecruitment(),
                from.getStatusDiary(),
                from.getStatusQuestionnaire(),
                from.getDiaryStart(),
                from.getDiaryEnd(),
                from.getAddress(),
                from.getPostcode(),
                from.getCity(),
                from.getRecruitmentStart(),
                from.getRecruitmentEnd(),
                from.getRecruitmentMinutesSpent(),
                from.getAcceptedInitialDiaryStart(),
                from.getHouseholdSize(),
                from.getRegion(),
                from.getMunicipalityNumber(),
                from.getDwellingNumber(),
                from.getHouseholdType(),
                from.getRespondents().stream()
                        .map(r -> RespondentResponse.map(r))
                        .collect(Collectors.toList())
        );
    }
}
