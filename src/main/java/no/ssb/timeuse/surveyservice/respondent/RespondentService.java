package no.ssb.timeuse.surveyservice.respondent;

import lombok.AllArgsConstructor;
import lombok.val;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.exception.ResourceValidationException;
import no.ssb.timeuse.surveyservice.household.Household;
import no.ssb.timeuse.surveyservice.household.HouseholdRepository;
import no.ssb.timeuse.surveyservice.utils.sample.SampleImport;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class RespondentService {

    private final RespondentRepository repository;
    private final HouseholdRepository householdRepository;

    public void validateRespondent(RespondentRequest request) {
        if (request.getName().isEmpty()) {
            throw new ResourceValidationException("Respondent must include a first and last name");
        }
    }

    public Respondent mapToDao(RespondentRequest request, Optional<UUID> respondentId) {
        validateRespondent(request);

        val newRespondent = Respondent.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .householdSequenceNumber(request.getHouseholdSequenceNumber())
                .age(request.getAge())
                .relationToRecruitmentRefPerson(request.getRelationToRecruitmentRefPerson())
                .education(request.getEducation())
                .build();

        if (respondentId.isPresent()) {
            val existingRespondent = repository.findByRespondentId(respondentId.get())
                    .orElseThrow(() -> new ResourceNotFoundException("Respondent with respondentId " + respondentId.get() + " does not exist"));
            newRespondent.setRespondentId(existingRespondent.getRespondentId());
            newRespondent.setId(existingRespondent.getId());
            newRespondent.setHousehold(existingRespondent.getHousehold());
            newRespondent.setAppointments(existingRespondent.getAppointments());
        }
        return newRespondent;
    }

    public Respondent mapToRespondentFromSample(SampleImport request) {

        Household household = householdRepository.findByIoNumber(Long.valueOf(request.getIoNumber()))
                .orElseThrow(() -> new ResourceNotFoundException("Household with ioNumber " + request.getIoNumber() + " does not exits"));

        return Respondent.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .householdSequenceNumber(Integer.valueOf(request.getHouseholdSequenceNumber()))
                .age(request.getAge())
                //.relationToRecruitmentRefPerson(request.getRelationToRecruitmentRefPerson())
                .education(request.getEducation())
                .respondentId(request.getNewUUID())
                .dateOfBirth(LocalDate.parse(request.getDateOfBirth(), DateTimeFormatter.ofPattern("yyyyMMdd")))
                .gender(request.getGender())
                .household(household)
                .build();
    }

//    public Respondent mapToRespondentFromBlaiseRespondent(BlaiseExportRespondent request, Long ioNumber) {
//
//        val household = householdRepository.findByIoNumber(ioNumber)
//                .orElseThrow(() -> new ResourceNotFoundException("Household with ioNumber " + ioNumber + " does not exits"));
//
//        val respondent = household.getRespondents().stream()
//                .filter(r -> r.getHouseholdSequenceNumber().equals(request.getHouseholdSequenceNumber()))
//                .findFirst()
//                .map(existingRespondent -> {
//                    existingRespondent.setName(request.getName());
//                    existingRespondent.setPhone(request.getPhone());
//                    existingRespondent.setEmail(request.getEmail());
//                    existingRespondent.setHouseholdSequenceNumber(request.getHouseholdSequenceNumber());
//                    existingRespondent.setAge(request.getAge());
//                    existingRespondent.setRelationToRecruitmentRefPerson(request.getRelationToRecruitmentRefPerson());
//                    existingRespondent.setDateOfBirth(request.getDateOfBirth());
//                    existingRespondent.setGender(request.getGender());
//                    return existingRespondent;
//                })
//                .orElse(Respondent.builder()
//                        .name(request.getName())
//                        .phone(request.getPhone())
//                        .email(request.getEmail())
//                        .householdSequenceNumber(request.getHouseholdSequenceNumber())
//                        .age(request.getAge()).relationToRecruitmentRefPerson(request.getRelationToRecruitmentRefPerson())
//                        .dateOfBirth(request.getDateOfBirth())
//                        .gender(request.getGender())
//                        .household(household)
//                        .respondentId(UUID.randomUUID())
//                        .build()
//                );
//
//        return respondent;
//    }
}
