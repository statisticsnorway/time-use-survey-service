package no.ssb.timeuse.surveyservice.respondent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.ssb.timeuse.surveyservice.household.Household;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RESPONDENT")
public class Respondent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "respondent_seq_generator")
    @SequenceGenerator(name="respondent_seq_generator", sequenceName="RESPONDENT_SEQ", allocationSize = 1)
    private Long id;

//    @OneToMany(mappedBy = "respondent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Purchase> purchases;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HOUSEHOLD_ID")
    private Household household;

    @Column(name = "RESPONDENT_UUID")
    private UUID respondentId;

    @Column(name = "HOUSEHOLD_SEQUENCE_NUMBER")
    private Integer householdSequenceNumber;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;

    @Column(name = "AGE")
    private Integer age;

    @Column(name = "RELATION_TO_RECRUITMENT_REF_PERSON")
    private String relationToRecruitmentRefPerson;

//    @OneToMany(mappedBy = "respondent", cascade = CascadeType.ALL)
//    private List<CommunicationLogEntry> communicationLogs;
//
//    @OneToMany(mappedBy = "respondent", cascade = CascadeType.ALL)
//    private List<Appointment> appointments;

}
