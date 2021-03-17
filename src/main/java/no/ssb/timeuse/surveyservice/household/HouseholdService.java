package no.ssb.timeuse.surveyservice.household;

import lombok.AllArgsConstructor;
import lombok.val;
import no.ssb.timeuse.surveyservice.utils.sample.SampleImport;
import org.springframework.stereotype.Component;
//import no.ssb.timeuse.surveyservice.blaise.BlaiseExportHousehold;


@Component
@AllArgsConstructor
public class HouseholdService {

    private final HouseholdRepository repository;

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
                .diaryStart(request.getDiaryStart())
                .diaryEnd(request.getDiaryEnd())
                .address(request.getAddress())
                .postcode(request.getPostcode())
                .city(request.getCity())
                .recruitmentStart(request.getRecruitmentStart())
                .recruitmentEnd(request.getRecruitmentEnd())
                .recruitmentMinutesSpent(request.getRecruitmentMinutesSpent())
                .acceptedInitialDiaryStart(request.getAcceptedInitialDiaryStart())
                .householdSize(request.getHouseholdSize())
                .region(request.getRegion())
                .municipalityNumber(request.getMunicipalityNumber())
                .dwellingNumber(request.getDwellingNumber())
                .householdType(request.getHouseholdType())
                .build();
    }

    public Household mapToHouseholdFromSample(SampleImport request) {

        if (request.getIsReferencePerson().equalsIgnoreCase("true")) {
            val household = repository.findByIoNumber(Long.valueOf(request.getIoNumber()))
                    .map(existingHousehold -> {
                        existingHousehold.setMainRespondentId(request.getNewUUID());
                        return existingHousehold;
                    })
                    .orElse(Household.builder()
                            .address(request.getAddress())
                            .householdType(request.getHouseholdType())
                            .householdSize(request.getHouseholdSize())
                            .dwellingNumber(request.getDwellingNumber())
                            .municipalityNumber(request.getMunicipalityNumber())
                            .region(request.getRegion())
                            .ioNumber(Long.valueOf(request.getIoNumber()))
                            .statusRecruitment("NOT_STARTED")
                            .statusQuestionnaire("NOT_STARTED")
                            .statusDiary("NOT_STARTED")
                            .statusSurvey("NOT_STARTED")
                            .city(request.getCity())
                            .postcode(request.getPostCode())
                            .mainRespondentId(request.getNewUUID())
                            .referenceWeek(Integer.valueOf(request.getReferenceWeek()))
                            .build()
                    );
            return household;

        } else if (repository.findByIoNumber(Long.valueOf(request.getIoNumber())).isEmpty()) {
            return Household.builder()
                    .address(request.getAddress())
                    .householdType(request.getHouseholdType())
                    .householdSize(request.getHouseholdSize())
                    .dwellingNumber(request.getDwellingNumber())
                    .municipalityNumber(request.getMunicipalityNumber())
                    .region(request.getRegion())
                    .ioNumber(Long.valueOf(request.getIoNumber()))
                    .statusRecruitment("NOT_STARTED")
                    .statusQuestionnaire("NOT_STARTED")
                    .statusDiary("NOT_STARTED")
                    .statusSurvey("NOT_STARTED")
                    .city(request.getCity())
                    .postcode(request.getPostCode())
                    .referenceWeek(Integer.valueOf(request.getReferenceWeek()))
                    .build();
        }
        return null;  // TODO: will this fail ?
    }


//    //TODO count householdSize
//    public Household mapToHouseholdFromBlaiseExport(BlaiseExportHousehold request) {
//        val household = repository.findByIoNumber(request.getIoNumber())
//                .map(updatedHousehold -> {
//                    updatedHousehold.setRecruitmentStart(request.getRecruitmentStart());
//                    updatedHousehold.setRecruitmentEnd(request.getRecruitmentEnd());
//                    updatedHousehold.setRecruitmentMinutesSpent(Integer.parseInt(request.getRecruitmentMinutesSpent()));
//                    updatedHousehold.setDiaryStart(request.getDiaryStart());
//                    updatedHousehold.setDiaryEnd(request.getDiaryEnd());
//                    updatedHousehold.setStatusRecruitment(CodeList.status.get("01").getValue());
//
//                    return updatedHousehold;
//                })
//                .orElseThrow(() -> new ResourceNotFoundException("Household with ioNumber " + request.getIoNumber() + " does not exits"));
//
//        return household;
//
//    }
}
