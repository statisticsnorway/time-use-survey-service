package no.ssb.timeuse.surveyservice.communicationlog.scheduled;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Category;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Direction;
import no.ssb.timeuse.surveyservice.communicationlog.enums.ScheduledType;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Type;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ScheduledCommunicationResponse {
    final Long id;
    final String resopondentCriteria;
    final ScheduledType scheduledType;
    final Direction direction;
    final LocalDateTime communicationTriggered;
    final LocalDateTime confirmedSent;
    final Type type;
    final Category category;
    final String message;
    final String createdBy;
    final LocalDateTime createdTime;
    final LocalDateTime scheduled;

    public static ScheduledCommunicationResponse map(ScheduledCommunication from) {

        return new ScheduledCommunicationResponse(
                from.getId(),
                from.getRespondentCriteria(),
                from.getScheduledType(),
                from.getDirection(),
                from.getCommunicationTriggered(),
                from.getConfirmedSent(),
                from.getType(),
                from.getCategory(),
                from.getMessage(),
                from.getCreatedBy(),
                from.getCreatedTime(),
                from.getScheduled());
    }
}
