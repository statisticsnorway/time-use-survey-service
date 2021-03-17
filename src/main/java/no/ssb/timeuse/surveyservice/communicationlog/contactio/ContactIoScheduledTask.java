package no.ssb.timeuse.surveyservice.communicationlog.contactio;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import no.ssb.timeuse.surveyservice.communicationlog.CommunicationLogEntry;
import no.ssb.timeuse.surveyservice.communicationlog.CommunicationLogRepository;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Direction;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Type;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
public class ContactIoScheduledTask {

    @Autowired
    CommunicationLogRepository repository;
    @Autowired
    ContactIoConsumer contactIo;

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
                    .collect(Collectors.toList()));
        }
    }

    private CommunicationLogEntry setConfirmedSent(CommunicationLogEntry entry) {
        entry.setConfirmedSent(LocalDateTime.now());
        return entry;
    }
}
