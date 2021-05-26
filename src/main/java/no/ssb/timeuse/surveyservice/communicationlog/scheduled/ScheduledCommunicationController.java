package no.ssb.timeuse.surveyservice.communicationlog.scheduled;

import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@AllArgsConstructor
@Timed
@Slf4j
@RequestMapping("/v1/scheduled-communication")
public class ScheduledCommunicationController {
    private final ScheduledCommunicationRepository repository;
    private final ScheduledCommunicationService service;

    @GetMapping
    public List<ScheduledCommunicationResponse> entries (){
        return repository.findAll().stream()
                    .map(ScheduledCommunicationResponse::map)
                    .collect(Collectors.toList());
    }

    @PostMapping
    public ScheduledCommunicationResponse createNewScheduledCommunication(
            @RequestBody ScheduledCommunicationRequest scheduledCommunicationRequest) {
        log.info("Post ScheduledCommunicationRequest: {}", scheduledCommunicationRequest);
        ScheduledCommunication saved = service.save(scheduledCommunicationRequest);
        log.info("saved scheduledcommunication: {}", saved);
        return ScheduledCommunicationResponse.map(service.save(scheduledCommunicationRequest));
    }

}
