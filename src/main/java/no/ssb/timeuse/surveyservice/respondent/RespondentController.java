package no.ssb.timeuse.surveyservice.respondent;


import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
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


@RestController
@AllArgsConstructor
@Timed
@RequestMapping("/v1/respondents")
public class RespondentController {

    private final RespondentRepository repository;
    private final RespondentService service;

    @CrossOrigin
    @GetMapping
    public List<RespondentResponse> entries() {
        return repository.findAll().stream()
                .map(r -> RespondentResponse.map(r))
                .collect(Collectors.toList());
    }

    @CrossOrigin
    @GetMapping("{respondentId}")
    public RespondentResponse findRespondentByRespondentId(@PathVariable UUID respondentId) {
        return repository.findByRespondentId(respondentId)
                .map(r -> RespondentResponse.map(r))
                .orElseThrow(() -> new ResourceNotFoundException("Respondent with respondentId " + respondentId + " does not exist"));
    }

    @CrossOrigin
    @PutMapping("{respondentId}")
    public RespondentResponse updateRespondent(@PathVariable UUID respondentId, @RequestBody RespondentRequest respondentRequest) {
        Respondent updatedRespondent = repository.save(service.mapToDao(respondentRequest, Optional.of(respondentId)));
        return RespondentResponse.map(updatedRespondent);
    }

}

