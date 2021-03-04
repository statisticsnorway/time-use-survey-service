package no.ssb.timeuse.surveyservice.respondent;

import lombok.AllArgsConstructor;
import lombok.val;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.exception.ResourceValidationException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class RespondentService {

    private final RespondentRepository repository;

    public  void validateRespondent(RespondentRequest request) {
        if (request.getFirstName().isEmpty() || request.getLastName().isEmpty()) {
            throw new ResourceValidationException("Respondent must include a first and last name");
        }
    }

    public Respondent mapToDao(RespondentRequest request, Optional<UUID> respondentId) {
        validateRespondent(request);

        val newRespondent = Respondent.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .householdSequenceNumber(request.getHouseholdSequenceNumber())
                .age(request.getAge())
                .relationToRecruitmentRefPerson(request.getRelationToRecruitmentRefPerson())
                .build();

        if (respondentId.isPresent()) {
            val existingRespondent = repository.findByRespondentId(respondentId.get())
                    .orElseThrow(() -> new ResourceNotFoundException("Respondent with respondentId " + respondentId.get() + " does not exist"));
            newRespondent.setRespondentId(existingRespondent.getRespondentId());
            newRespondent.setId(existingRespondent.getId());
            newRespondent.setHousehold(existingRespondent.getHousehold());
//            newRespondent.setAppointments(existingRespondent.getAppointments());
        }
        return newRespondent;
    }


}
