package no.ssb.timeuse.surveyservice.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.ssb.timeuse.surveyservice.interviewer.Interviewer;
import no.ssb.timeuse.surveyservice.respondent.Respondent;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "APPOINTMENT")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointment_seq_generator")
    @SequenceGenerator(name="appointment_seq_generator", sequenceName="APPOINTMENT_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "RESPONDENT_ID", nullable = false)
    private Respondent respondent;

    @Column(name = "APPOINTMENT_TIME")
    private LocalDateTime appointmentTime;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "INTERVIEWER_ID")
    private Interviewer interviewer;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_TIME")
    private LocalDateTime createdTime;

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", respondentId=" + respondent.getRespondentId() +
                ", appointmentTime=" + appointmentTime +
                ", description='" + description + '\'' +
                ", interviewerId='" + (interviewer != null ? interviewer.getInterviewerId() : null ) + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdTime='" + createdTime + '\'' +
                '}';
    }
}
