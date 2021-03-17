package no.ssb.timeuse.surveyservice.communicationlog;

import lombok.AllArgsConstructor;
import lombok.val;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.respondent.RespondentRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CommunicationLogService {
    private final RespondentRepository respondentRepository;
    private final CommunicationLogRepository repository;

    public List<CommunicationLogEntry> save(CommunicationLogEntryRequest request) {
        val respondents = respondentRepository.findAllByRespondentIdIn(request.getRespondentId());

        // If the request contains respondentIds that are not found in respondentRepository, throw an exception with the missing respondentIds
        if(respondents.isEmpty() || request.getRespondentId().size() > respondents.size()) {
            val missingRespondentIds = request.getRespondentId().stream()
                    .filter(requestedRespondentId -> respondents.stream().noneMatch(foundRespondents -> foundRespondents.getRespondentId() == requestedRespondentId))
                    .collect(Collectors.toList());
            throw new ResourceNotFoundException("One or several respondentIDs was not found: " + missingRespondentIds.toString());
        }

        // Create CommunicationLogEntries for each respondent
        val daos = new ArrayList<CommunicationLogEntry>();
        respondents.forEach(respondent -> daos.add(CommunicationLogEntry.builder()
                .respondent(respondent)
                .direction(request.getDirection())
                .type(request.getType())
                .category(request.getCategory())
                .message(request.getMessage())
                .createdBy(request.getCreatedBy())
                .scheduled(request.getScheduled())
                .communicationTriggered(LocalDateTime.now())
                .build()
        ));

        return repository.saveAll(daos);
    }
}
