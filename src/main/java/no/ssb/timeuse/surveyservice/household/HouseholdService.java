package no.ssb.timeuse.surveyservice.household;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class HouseholdService {

    public Household convertedToHousehold(Long id, HouseholdRequest request) {
        return Household.builder()
                .id(id)
                .referenceWeek(request.getReferenceWeek())
                .mainRespondentId(request.getMainRespondentId())
                .ioNumber(request.getIoNumber())
                .statusSurvey(request.getStatusSurvey())
                .statusDiary(request.getStatusDiary())
                .statusQuestionnaire(request.getStatusQuestionnaire())
                .statusRecruitment(request.getStatusRecruitment())
                .diaryStart(LocalDate.parse(request.getDiaryStart()))
                .diaryEnd(LocalDate.parse(request.getDiaryEnd()))
                .address(request.getAddress())
                .postcode(request.getPostcode())
                .city(request.getCity())
                .recruitmentStart(LocalDate.parse(request.getRecruitmentStart()))
                .recruitmentEnd(LocalDate.parse(request.getRecruitmentEnd()))
                .recruitmentMinutesSpent(request.getRecruitmentMinutesSpent())
                .acceptedInitialDiaryStart(request.getAcceptedInitialDiaryStart())
                .householdSize(request.getHouseholdSize())
                .build();
    }
}
