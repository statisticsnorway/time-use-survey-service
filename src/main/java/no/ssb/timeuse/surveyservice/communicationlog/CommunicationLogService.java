package no.ssb.timeuse.surveyservice.communicationlog;

import lombok.AllArgsConstructor;
import lombok.val;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.respondent.Respondent;
import no.ssb.timeuse.surveyservice.respondent.RespondentRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CommunicationLogService {
    private final RespondentRepository respondentRepository;
    private final CommunicationLogRepository repository;

    public List<CommunicationLogEntry> save(CommunicationLogEntryRequest request) {
        val matcher = MessageBuilderPattern.getMatcher(request.getMessage());
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
        respondents.forEach(respondent -> {
            daos.add(CommunicationLogEntry.builder()
                    .respondent(respondent)
                    .direction(request.getDirection())
                    .type(request.getType())
                    .category(request.getCategory())
                    .message(buildMessage(respondent, matcher))
                    .createdBy(request.getCreatedBy())
                    .scheduled(request.getScheduled())
                    .communicationTriggered(LocalDateTime.now())
                    .build()
            );
        });

        return repository.saveAll(daos);
    }

    private String buildMessage(Respondent respondent, Matcher matcher) {
        StringBuffer stringBuffer = new StringBuffer();
        Map<String, String> respondentValues = getRespondentFieldMap(respondent);
        while(matcher.find()) {
            matcher.appendReplacement(stringBuffer, respondentValues.get(matcher.group(1)));
        }
        matcher.appendTail(stringBuffer);
        matcher.reset();
        return stringBuffer.toString();
    }

    //TODO what should really happen if expenditure period is null?
    private Map<String, String> getRespondentFieldMap(Respondent respondent) {
        val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        val dateFrom = respondent.getDiaryStart();
        val dateTo = respondent.getDiaryEnd();
        return Map.of(
//                "{email}", respondent.getEmail(), //removed in consumtion's suvey-service 08.04.2021
                "{name}", respondent.getName(),
                "{ioNumber}", respondent.getIoNumber().toString(),
                "{dateFrom}", dateFrom != null ? dateFrom.format(dateFormat) : "",
                "{dateTo}", dateTo != null ? dateTo.format(dateFormat) : "");

    }

}
