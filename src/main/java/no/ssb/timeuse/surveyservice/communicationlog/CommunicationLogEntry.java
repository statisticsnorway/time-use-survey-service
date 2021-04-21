package no.ssb.timeuse.surveyservice.communicationlog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Category;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Direction;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Type;
import no.ssb.timeuse.surveyservice.respondent.Respondent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "COMMUNICATION_LOG")
public class CommunicationLogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "communication_log_seq_generator")
    @SequenceGenerator(name="communication_log_seq_generator", sequenceName="COMMUNICATION_LOG_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RESPONDENT_ID")
    private Respondent respondent;

    @Column(name = "DIRECTION", nullable = false)
    @Enumerated(EnumType.STRING)
    private Direction direction;

    @Column(name = "COMMUNICATION_TRIGGERED")
    private LocalDateTime communicationTriggered;

    @Column(name = "CONFIRMED_SENT")
    private LocalDateTime confirmedSent;

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
}


