package no.ssb.timeuse.surveyservice.communicationlog.scheduled;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import no.ssb.timeuse.surveyservice.communicationlog.CommunicationLogEntry;
import no.ssb.timeuse.surveyservice.communicationlog.CommunicationLogRepository;
import no.ssb.timeuse.surveyservice.communicationlog.contactRespondent.ContactRespondentConsumer;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Direction;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Type;
import no.ssb.timeuse.surveyservice.respondent.Respondent;
import no.ssb.timeuse.surveyservice.respondent.RespondentRepository;
import no.ssb.timeuse.surveyservice.search.RespondentSearchRepository;
import no.ssb.timeuse.surveyservice.search.SearchRequestDeserializer;
import no.ssb.timeuse.surveyservice.search.SearchRequestGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class ScheduledCommunicationScheduledTask {

    @Autowired
    ScheduledCommunicationRepository scheduledCommunicationRepository;
    @Autowired
    CommunicationLogRepository communicationLogRepository;
    @Autowired
    RespondentRepository respondentRepository;

    @Autowired
    ContactRespondentConsumer contactIo;

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void run() {
        log.info("get scheduledCommunications");
        val unsentEntries = scheduledCommunicationRepository.findAllByConfirmedSentIsNullAndScheduledIsBefore(LocalDateTime.now())
                .stream()
                .filter(entry -> (entry.getDirection() == Direction.OUTGOING) && (entry.getType() == Type.EMAIL || entry.getType() == Type.SMS))
                .collect(Collectors.toList());
        ObjectMapper objectMapper = new ObjectMapper();
        if(!unsentEntries.isEmpty()) {
            log.info("Found " + unsentEntries.size() + " unsent scheduled communication entries.");

            unsentEntries.forEach((entry) -> {
                entry.setCommunicationTriggered(LocalDateTime.now());
                log.info("kriterier: {}", entry.getRespondentCriteria());
                SearchRequestGroup searchRequestGroup = null;
                try {
                    log.info("convert criteria to searchrequestGroup");
                    searchRequestGroup = objectMapper.readValue(entry.getRespondentCriteria(), SearchRequestGroup.class);
                    log.info("searchRequestGroup: {}", searchRequestGroup);
                    List<Respondent> respondents = respondentRepository.searchForRespondents(searchRequestGroup);
                    log.info("respondents: {}", respondents.size());

                    List<CommunicationLogEntry> newCommunicationLogs = new ArrayList<>();
                    respondents.forEach(respondent -> {
                        CommunicationLogEntry communicationLogEntry = createCommunicationLogEntryForRespondent(entry, respondent);
                        newCommunicationLogs.add(communicationLogEntry);
                    });
                    List<CommunicationLogEntry> savedCommunicationLogs = communicationLogRepository.saveAll(newCommunicationLogs);
                    log.info("created {} new communicationLogEntries for criteria {}",
                            savedCommunicationLogs.size(), entry.getRespondentCriteria());
                    entry.setConfirmedSent(LocalDateTime.now());
                } catch (JsonProcessingException e) {
                    log.error("Error converting respondentcriteria to searchrequestgroup ({})", entry.getRespondentCriteria());
                }

            });

        }
        scheduledCommunicationRepository.saveAll(unsentEntries);
        log.info("finished treating {} unsent criteria-communications", unsentEntries.size());
    }

    private CommunicationLogEntry createCommunicationLogEntryForRespondent(ScheduledCommunication entry, Respondent respondent) {
        return CommunicationLogEntry.builder()
                .respondent(respondent)
                .scheduled(entry.getScheduled())
                .category(entry.getCategory())
                .direction(entry.getDirection())
                .createdTime(entry.getCreatedTime())
                .createdBy(entry.getCreatedBy())
                .message(entry.getMessage())
                .type(entry.getType())
                .build();

    }

//    private SearchRequestGroup convertFromCriteriaToSearchRequest(String respondentCriteria) throws JsonProcessingException {
//        log.info("convert {}", respondentCriteria);
//        respondentCriteria.
//
//        log.info("converted searchRequestGroup: {}", searchRequestGroup);
//        return searchRequestGroup;
//    }
}
