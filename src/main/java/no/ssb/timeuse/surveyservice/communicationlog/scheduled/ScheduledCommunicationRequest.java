package no.ssb.timeuse.surveyservice.communicationlog.scheduled;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Category;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Direction;
import no.ssb.timeuse.surveyservice.communicationlog.enums.ScheduledType;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Type;
import no.ssb.timeuse.surveyservice.search.SearchRequestGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ScheduledCommunicationRequest {
    private SearchRequestGroup respondentCriteria;
    private ScheduledType scheduledType;
    private Direction direction;
    private Type type;
    private Category category;
    private String message;
    private String createdBy;
    private LocalDateTime createdTime;
    private LocalDateTime scheduled;

}

