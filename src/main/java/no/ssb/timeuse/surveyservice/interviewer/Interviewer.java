package no.ssb.timeuse.surveyservice.interviewer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.ssb.timeuse.surveyservice.appointment.Appointment;
import no.ssb.timeuse.surveyservice.respondent.Respondent;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "INTERVIEWER")
public class Interviewer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "interviewer_seq_generator")
    @SequenceGenerator(name="interviewer_seq_generator", sequenceName="INTERVIEWER_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "INTERVIEWER_UUID")
    private UUID interviewerId;

    // Personal information
    @Column(name = "INITIALS", unique = true)
    private String initials;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PHONE")
    private String phone;

    @OneToMany(mappedBy = "interviewer", cascade = CascadeType.DETACH)
    private List<Respondent> respondents;

//    @OneToMany(mappedBy = "interviewer", cascade = CascadeType.ALL)
//    private List<CommunicationLogEntry> communicationLogs;

    @OneToMany(mappedBy = "interviewer", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    @Override
    public String toString() {
        return "Interviewer{" +
                "id=" + id +
                ", interviewerId=" + interviewerId +
                ", initials='" + initials + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
//                ", respondents=" + (respondents != null ? respondents.size() : 0) +
//                ", appointments=" + (appointments != null ? appointments.size() : 0) +
                '}';
    }
}
