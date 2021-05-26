package no.ssb.timeuse.surveyservice.communicationlog.scheduled;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Category;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Direction;
import no.ssb.timeuse.surveyservice.communicationlog.enums.ScheduledType;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "SCHEDULED_COMMUNICATION")
public class ScheduledCommunication {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sceduled_communication_seq_generator")
    @SequenceGenerator(name="scheduled_communication_seq_generator", sequenceName="SCHEDULED_COMMUNICATION_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "RESPONDENT_CRITERIA", nullable = false, length = 16000)
    private String respondentCriteria;

    @Column(name = "SCHEDULED_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private ScheduledType scheduledType;

    @Column(name = "DIRECTION", nullable = false)
    @Enumerated(EnumType.STRING)
    private Direction direction;

    @Column(name = "TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "CATEGORY", nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "MESSAGE", nullable = false)
    private String message;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_TIME")
    private LocalDateTime createdTime;

    @Column(name = "SCHEDULED")
    private LocalDateTime scheduled;

    @Column(name = "COMMUNICATION_TRIGGERED")
    private LocalDateTime communicationTriggered;

    @Column(name = "CONFIRMED_SENT")
    private LocalDateTime confirmedSent;

}
