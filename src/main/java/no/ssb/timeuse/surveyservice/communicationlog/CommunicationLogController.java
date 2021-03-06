package no.ssb.timeuse.surveyservice.communicationlog;

import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.ssb.timeuse.surveyservice.communicationlog.contactRespondent.ContactRespondentConsumer;
import no.ssb.timeuse.surveyservice.exception.MethodNotAllowedException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@AllArgsConstructor
@Timed
@Slf4j
@RequestMapping("/v1/communication-log-entries")
public class CommunicationLogController {
    private final CommunicationLogRepository repository;
    private final CommunicationLogService service;
    private final ContactRespondentConsumer consumer;

    @GetMapping
    public List<CommunicationLogEntryResponse> entries(@RequestParam (required = false) Optional<UUID> respondentId) {
        if(respondentId.isPresent()) {
            return repository.findByRespondentRespondentId(respondentId.get()).stream()
                    .map(CommunicationLogEntryResponse::map)
                    .collect(Collectors.toList());
        } else {
            throw new MethodNotAllowedException("You must provide a respondentId (UUID)");
        }
    }

    @PostMapping
    public List<CommunicationLogEntryResponse> createNewCommunicationLogs(@RequestBody CommunicationLogEntryRequest communicationLogEntryRequest) {
        log.info("communicationLogEntryRequest: {}", communicationLogEntryRequest);
        return service.save(communicationLogEntryRequest).stream()
                .map(CommunicationLogEntryResponse::map)
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    public List<CommunicationLogEntryResponse> allEntries() {
        return repository.findAll().stream()
                .map(CommunicationLogEntryResponse::map)
                .collect(Collectors.toList());
    }

    @GetMapping("/test")
    public void test() {
        log.info("Calling Contact IO...");
        consumer.call(repository.findAllByConfirmedSentIsNull());
    }
}
