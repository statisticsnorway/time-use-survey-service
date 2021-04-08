package no.ssb.timeuse.surveyservice.respondent.status;

import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.respondent.Respondent;
import no.ssb.timeuse.surveyservice.respondent.RespondentRepository;
import no.ssb.timeuse.surveyservice.respondent.RespondentRepository;
import no.ssb.timeuse.surveyservice.respondent.RespondentResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@Slf4j
@Timed
@AllArgsConstructor
@RequestMapping("/v1/respondent-status")
public class RespondentStatusController {

    private final RespondentRepository respondentRepository;

    @PostMapping
    public void updateRespondentStatus(@RequestBody RespondentStatusRequest from) {
        Respondent respondent = respondentRepository.findByRespondentId(from.getRespondentId())
                             .orElseThrow(() -> new ResourceNotFoundException(
                                     "Respondent with Id " + from.getRespondentId() + " does not exist"));

        respondent.setStatusQuestionnaire(from.getStatusQuestionnaire());
        respondent.setStatusDiary(from.getStatusDiary());
        respondentRepository.save(respondent);
    }
}
