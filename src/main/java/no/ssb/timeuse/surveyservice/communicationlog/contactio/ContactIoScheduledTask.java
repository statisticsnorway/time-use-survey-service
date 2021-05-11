package no.ssb.timeuse.surveyservice.communicationlog.contactio;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import no.ssb.timeuse.surveyservice.communicationlog.CommunicationLogEntry;
import no.ssb.timeuse.surveyservice.communicationlog.CommunicationLogRepository;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Category;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Direction;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Type;
import no.ssb.timeuse.surveyservice.respondent.Respondent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
public class ContactIoScheduledTask {

    @Autowired
    CommunicationLogRepository repository;
    @Autowired
    ContactIoConsumer contactIo;

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void run() {
        val unsentEntries = repository.findAllByConfirmedSentIsNull().stream()
                .filter(entry -> (entry.getDirection() == Direction.OUTGOING) && (entry.getType() == Type.EMAIL || entry.getType() == Type.SMS))
                .collect(Collectors.toList());

        if(!unsentEntries.isEmpty()) {
            log.info("Found " + unsentEntries.size() + " unsent communication log entries.");

            val confirmedSentIds = contactIo.call(unsentEntries);
            if (unsentEntries.size() > confirmedSentIds.size()) {
                log.error("ContactIO did not send all requested messages! Requested: " + unsentEntries.size() + ". Confirmed sent: " + confirmedSentIds.size());
            }

            repository.saveAll(repository.findAllByIdIn(confirmedSentIds).stream()
                    .map(this::setConfirmedSent)
                    .map(this::updateSurveyStatus)
                    .collect(Collectors.toList()));
        }
    }

    private CommunicationLogEntry setConfirmedSent(CommunicationLogEntry entry) {
        entry.setConfirmedSent(LocalDateTime.now());
        return entry;
    }

    private CommunicationLogEntry updateSurveyStatus(CommunicationLogEntry entry) {
        if(entry.getCategory() == Category.LOGININFO && entry.getDirection() == Direction.OUTGOING) {
            entry.getRespondent().setStatusSurvey("LOGININFO_SENT"); }
        return entry;
    }
}
