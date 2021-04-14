package no.ssb.timeuse.surveyservice.respondent;

import lombok.AllArgsConstructor;
import lombok.val;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.exception.ResourceValidationException;
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

    public void validateRespondent(RespondentRequest request) {
        if (request.getName().isEmpty()) {
            throw new ResourceValidationException("Respondent must include a first and last name");
        }
    }

    public Respondent mapToDao(RespondentRequest request, Optional<UUID> respondentId) {
        validateRespondent(request);

        val newRespondent = Respondent.builder()
                .name(request.getName())
                .ioNumber(request.getIoNumber())
                .phone(request.getPhone())
                .email(request.getEmail())
                .dateOfBirth(LocalDate.parse(request.getDateOfBirth(), DateTimeFormatter.ofPattern("[yyyy-MM-dd][yyyyMMdd]")))
                .age(request.getAge())
                .gender(request.getGender())
                .education(request.getEducation())
                .address(request.getAddress())
                .region(request.getRegion())
                .city(request.getCity())
                .postcode(request.getPostcode())
                .dwellingNumber(request.getDwellingNumber())
                .municipalityNumber(request.getMunicipalityNumber())
                .statusRecruitment(request.getStatusRecruitment())
                .statusQuestionnaire(request.getStatusQuestionnaire())
                .statusDiary(request.getStatusDiary())
                .statusSurvey(request.getStatusSurvey())
                .diaryStart(request.getDiaryStart())
                .diaryEnd(request.getDiaryEnd())
                .build();

        if (respondentId.isPresent()) {
            val existingRespondent = repository.findByRespondentId(respondentId.get())
                    .orElseThrow(() -> new ResourceNotFoundException("Respondent with respondentId " + respondentId.get() + " does not exist"));
            newRespondent.setRespondentId(existingRespondent.getRespondentId());
            newRespondent.setId(existingRespondent.getId());
            newRespondent.setAppointments(existingRespondent.getAppointments());
        }
        return newRespondent;
    }

    public Respondent mapToRespondentFromSample(SampleImport request) {

        return Respondent.builder()
                .ioNumber(Long.valueOf(request.getIoNumber()))
                .respondentId(request.getNewUUID())
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .dateOfBirth(LocalDate.parse(request.getDateOfBirth(), DateTimeFormatter.ofPattern("[yyyy-MM-dd][yyyyMMdd]")))
                .age(request.getAge())
                .gender(request.getGender())
                .education(request.getEducation())
                .address(request.getAddress())
                .region(request.getRegion())
                .city(request.getCity())
                .postcode(request.getPostCode())
                .dwellingNumber(request.getDwellingNumber())
                .municipalityNumber(request.getMunicipalityNumber())
                .statusRecruitment(request.getStatusRecruitment())
                .statusQuestionnaire(request.getStatusQuestionnaire())
                .statusDiary(request.getStatusDiary())
                .statusSurvey(request.getStatusSurvey())
                .diaryStart(LocalDate.parse(request.getDiaryStart(), DateTimeFormatter.ofPattern("[yyyy-MM-dd][yyyyMMdd]")))
                .diaryEnd(LocalDate.parse(request.getDiaryEnd(), DateTimeFormatter.ofPattern("[yyyy-MM-dd][yyyyMMdd]")))
                .build();
    }

}
