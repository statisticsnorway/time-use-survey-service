package no.ssb.timeuse.surveyservice.utils.sample;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import no.ssb.timeuse.surveyservice.activitiy.ActivityCategory;
import no.ssb.timeuse.surveyservice.activitiy.ActivityCategoryRepository;
import no.ssb.timeuse.surveyservice.exception.ResourceValidationException;
import no.ssb.timeuse.surveyservice.interviewer.InterviewerRepository;
import no.ssb.timeuse.surveyservice.interviewer.InterviewerService;
import no.ssb.timeuse.surveyservice.respondent.RespondentRepository;
import no.ssb.timeuse.surveyservice.respondent.RespondentService;
import no.ssb.timeuse.surveyservice.respondent.idmapper.RespondentIdMapperRepository;
import no.ssb.timeuse.surveyservice.respondent.idmapper.RespondentIdMapperService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@Slf4j
@AllArgsConstructor
@RequestMapping("/v1/sample-import")
public class SampleImportController {

    RespondentRepository respondentRepository;
    RespondentService respondentService;
    RespondentIdMapperRepository respondentIdMapperRepository;
    RespondentIdMapperService respondentIdMapperService;
    ActivityCategoryRepository activityCategoryRepository;

    InterviewerRepository interviewerRepository;
    InterviewerService interviewerService;

    @PostMapping
    public void importRespondents(@RequestBody List<SampleImport> from) {

        // First do some validation and manipulation.
        List<SampleImport> updatedFrom = from;
        updatedFrom.stream().forEach(r -> {
            validateSampleImport(r);
            if(r.getNewUUID()==null) {
                r.setNewUUID(UUID.randomUUID());
            }
            if (r.getCareOfAddress() != null && !r.getCareOfAddress().isEmpty()) {
                if (r.getAddress() != null && !r.getAddress().isEmpty()) {
                    r.setAddress(r.getCareOfAddress() + ", " + r.getAddress());
                }
                else {
                    r.setAddress(r.getCareOfAddress());
                }
            }
        });

        // Create Respondent
        val newRespondents = updatedFrom.stream()
                .map(r -> respondentService.mapToRespondentFromSample(r))
                .collect(Collectors.toList());

        respondentRepository.saveAll(newRespondents);
        log.info("Respondents imported: " + newRespondents.size());

        // Map FNR to UUID (Respondent_mapper)
        val newMapper = updatedFrom.stream()
                .map(r -> respondentIdMapperService.mapFromSample(r))
                .collect(Collectors.toList());

        respondentIdMapperRepository.saveAll(newMapper);

    }


    @PostMapping("/clean")
    public void cleanRespondents() {
        respondentRepository.deleteAll();
    }


    public void validateSampleImport(SampleImport request) {
        try {
            Long.parseLong(request.getIoNumber());
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd][yyyyMMdd]");
            LocalDate.parse(request.getDateOfBirth(), dateTimeFormatter);
            LocalDate.parse(request.getDiaryStart(), dateTimeFormatter);
            LocalDate.parse(request.getDiaryEnd(), dateTimeFormatter);
        } catch (Exception e) {
            throw new ResourceValidationException("SampleImport with ioNumber " + request.getIoNumber() + " is invalid");
        }
    }

    @PostMapping("/activityCategories")
    public void importActivityCategories(@RequestBody List<ActivityCategory> activityCategories) {
        List<ActivityCategory> res = activityCategoryRepository.saveAll(activityCategories);
    }


    @PostMapping("/importInterviewers")
    public void importInterviewers(@RequestBody List<SampleImportInterviewer> from) {

        // First do some validation and manipulation.
        List<SampleImportInterviewer> updatedFrom = from;
        updatedFrom.stream().forEach(r -> {
            r.setNewUUID(UUID.randomUUID());
        });

        // Create Respondent
        val newInterviewers = updatedFrom.stream()
                .map(r -> interviewerService.mapToInterviewerFromSample(r))
                .collect(Collectors.toList());

        interviewerRepository.saveAll(newInterviewers);
        log.info("Interviewers imported: " + newInterviewers.size());

    }


}
