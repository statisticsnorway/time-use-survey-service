package no.ssb.timeuse.surveyservice.respondent;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import no.ssb.timeuse.surveyservice.diary.Diary;
import no.ssb.timeuse.surveyservice.respondent.diarystarthistory.DiaryStartHistory;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.exception.ResourceValidationException;
import no.ssb.timeuse.surveyservice.interviewer.InterviewerRepository;
import no.ssb.timeuse.surveyservice.utils.sample.SampleImport;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Component
@AllArgsConstructor
@Slf4j
public class RespondentService {

    private final RespondentRepository repository;
    private final InterviewerRepository interviewerRepository;

    public boolean validateRespondent(RespondentRequest request) {
        if (request.getName().isEmpty()) {
            throw new ResourceValidationException("Respondent must include a first and last name");
        }
        return true;
    }

    public Respondent mapToDao(RespondentRequest request, Optional<UUID> respondentId) {
        validateRespondent(request);

        val newRespondent = Respondent.builder()
                .name(request.getName())
                .ioNumber(request.getIoNumber())
                .phone(request.getPhone())
                .email(request.getEmail())
                .dateOfBirth(request.getDateOfBirth() != null ? LocalDate.parse(request.getDateOfBirth(), DateTimeFormatter.ofPattern("[yyyy-MM-dd][yyyyMMdd]")) : null)
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
            if (request.getInterviewerId() == null) {
                newRespondent.setInterviewer(null);
            } else if ( existingRespondent.getInterviewer() != null &&
                    request.getInterviewerId().equals(existingRespondent.getInterviewer().getInterviewerId())) {
                newRespondent.setInterviewer(existingRespondent.getInterviewer());
            } else {
                val newInterviewer = interviewerRepository.findByInterviewerId(request.getInterviewerId())
                        .orElseThrow(() -> new ResourceNotFoundException("Interviewer with interviewerId " + request.getInterviewerId() + " does not exist"));
                newRespondent.setInterviewer(newInterviewer);
            }

            log.info("ny diarystart: {}, eksisterende: {}", newRespondent.getDiaryStart(), existingRespondent.getDiaryStart());
            if (!newRespondent.getDiaryStart().equals(existingRespondent.getDiaryStart())) {
                log.info("ulike startdatoer");
                val diaryStartHistory = DiaryStartHistory.builder()
                        .respondent(existingRespondent)
                        .diaryStart(existingRespondent.getDiaryStart())
                        .build();
                List<DiaryStartHistory> respondentDiaryStartHistories = newRespondent.getDiaryStartHistories() != null ?
                        newRespondent.getDiaryStartHistories() : new ArrayList<DiaryStartHistory>(0);
                log.info("history: {}", respondentDiaryStartHistories);
                respondentDiaryStartHistories.add(diaryStartHistory);
                log.info("etter adding: {}", respondentDiaryStartHistories);

                newRespondent.setDiaryStartHistories(respondentDiaryStartHistories);
            }
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
                .diaryStartOrig(LocalDate.parse(request.getDiaryStart(), DateTimeFormatter.ofPattern("[yyyy-MM-dd][yyyyMMdd]")))
                .build();
    }

}
