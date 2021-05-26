package no.ssb.timeuse.surveyservice.communicationlog.scheduled;

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

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void run() {
        log.info("get scheduledCommunications");
//        val unsentEntries = scheduledCommunicationRepository.findAllByConfirmedSentIsNullAndScheduledIsBefore(LocalDateTime.now())
        val unsentEntries = scheduledCommunicationRepository.findAll()
                .stream()
//                .filter(entry -> (entry.getDirection() == Direction.OUTGOING) && (entry.getType() == Type.EMAIL || entry.getType() == Type.SMS))
                .collect(Collectors.toList());
        if(!unsentEntries.isEmpty()) {
            log.info("Found " + unsentEntries.size() + " unsent scheduled communication entries.");

            unsentEntries.forEach((entry) -> {
                entry.setCommunicationTriggered(LocalDateTime.now());
                log.info("kriterier: {}", entry.getRespondentCriteria());
                SearchRequestGroup searchRequestGroup = convertFromCriteriaToSearchRequest(entry.getRespondentCriteria());
                List<Respondent> respondents = respondentRepository.searchForRespondents(searchRequestGroup);

                List<CommunicationLogEntry> newCommunicationLogs = new ArrayList<>();
                respondents.forEach(respondent -> {
                    CommunicationLogEntry communicationLogEntry = createCommunicationLogEntryForRespondent(entry, respondent);
                    newCommunicationLogs.add(communicationLogEntry);
                });
                List<CommunicationLogEntry> savedCommunicationLogs = communicationLogRepository.saveAll(newCommunicationLogs);
                log.info("created {} new communicationLogEntries for criteria {}",
                        savedCommunicationLogs.size(), entry.getRespondentCriteria());
                entry.setConfirmedSent(LocalDateTime.now());
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

    private SearchRequestGroup convertFromCriteriaToSearchRequest(String respondentCriteria) {
        log.info("convert {}", respondentCriteria);
//        SearchRequestGroup searchRequestGroup = new SearchRequestDeserializer(respondentCriteria);
        return new SearchRequestGroup();
    }
}
