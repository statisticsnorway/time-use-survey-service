package no.ssb.timeuse.surveyservice.communicationlog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Category;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Direction;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Type;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class CommunicationLogEntryResponse {
    final Long id;
    final Direction direction;
    final LocalDateTime communicationTriggered;
    final LocalDateTime confirmedSent;
    final Type type;
    final Category category;
    final String message;
    final String createdBy;
    final LocalDateTime createdTime;
    final LocalDateTime scheduled;
    final UUID respondentId;
    final String respondentPhoneNumber;
    final String respondentEmailAddress;

    public static CommunicationLogEntryResponse map(CommunicationLogEntry from) {

        return new CommunicationLogEntryResponse(
                from.getId(),
                from.getDirection(),
                from.getCommunicationTriggered(),
                from.getConfirmedSent(),
                from.getType(),
                from.getCategory(),
                from.getMessage(),
                from.getCreatedBy(),
                from.getCreatedTime(),
                from.getScheduled(),
                from.getRespondent().getRespondentId(),
                from.getRespondent().getPhone(),
                from.getRespondent().getEmail());
    }
}
