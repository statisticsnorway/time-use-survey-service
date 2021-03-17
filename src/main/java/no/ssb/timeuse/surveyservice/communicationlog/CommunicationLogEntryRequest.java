package no.ssb.timeuse.surveyservice.communicationlog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Category;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Direction;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Type;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunicationLogEntryRequest {
    private Direction direction;
    private Type type;
    private Category category;
    private String message;
    private Set<UUID> respondentId;
    private String createdBy;
    private LocalDateTime scheduled;

}

