package no.ssb.timeuse.surveyservice.respondent;


import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@CrossOrigin
@RestController
@Slf4j
@AllArgsConstructor
@Timed
@RequestMapping("/v1/respondents")
public class RespondentController {

    private final RespondentRepository repository;
    private final RespondentService service;

    @GetMapping
    public List<RespondentResponse> entries() {
        return repository.findAll().stream()
                .map(RespondentResponse::map)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<String> saveAll(@RequestBody List<Respondent> respondents) {
        int saved = repository.saveAll(respondents).size();
        return new ResponseEntity<>("Saved " + saved + " dents to base.", HttpStatus.OK);
    }

    @GetMapping("{respondentId}")
    public RespondentResponse findRespondentByRespondentId(@PathVariable UUID respondentId) {
        log.info("get respondent with respondentId {}", respondentId);
        return repository.findByRespondentId(respondentId)
                .map(RespondentResponse::map)
                .orElseThrow(() -> new ResourceNotFoundException("Respondent with respondentId " + respondentId + " does not exist"));
    }

    @PutMapping("{respondentId}")
    public RespondentResponse updateRespondent(@PathVariable UUID respondentId, @RequestBody RespondentRequest respondentRequest) {
        log.info("update respondent {}: {}", respondentId, respondentRequest);

        Respondent updatedRespondent = repository.save(service.mapToDao(respondentRequest, Optional.of(respondentId)));
        log.info("put respondent: {}", updatedRespondent);
        return RespondentResponse.map(updatedRespondent);
    }


    @GetMapping("/respondent/{ioNumber}")
    public RespondentResponse getByIoNumber(@PathVariable Long ioNumber) {
        return repository.findByIoNumber(ioNumber).map(RespondentResponse::map).
                orElseThrow(() -> new ResourceNotFoundException("Respondent with IO-number " + ioNumber + " does not exist"));
    }


    @GetMapping("/interviewer/{interviewerId}")
    public List<RespondentResponse> getByInterviewerId(@PathVariable UUID interviewerId) {
        return repository.findAllByInterviewerInterviewerId(interviewerId).stream()
                .map(RespondentResponse::map)
                .collect(Collectors.toList());
    }

    @DeleteMapping("{respondentId}")
    public ResponseEntity<String> deleteRespondentByRespondentId(@PathVariable UUID respondentId) {
        Respondent deleteRespondent = repository.findByRespondentId(respondentId)
                .orElseThrow(() -> new ResourceNotFoundException("Respondent (to be deleted) with respondentId " + respondentId + " does not exist"));
        repository.deleteById(deleteRespondent.getId());
        return new ResponseEntity<>("Respondent with respondentID " + respondentId + " deleted.", HttpStatus.OK);
    }

}

