package no.ssb.timeuse.surveyservice.respondent;


import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
                .map(r -> RespondentResponse.map(r))
                .collect(Collectors.toList());
    }

    @GetMapping("{respondentId}")
    public RespondentResponse findRespondentByRespondentId(@PathVariable UUID respondentId) {
        log.info("get respondent with respondentId {}", respondentId);
        return repository.findByRespondentId(respondentId)
                .map(r -> RespondentResponse.map(r))
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

}

