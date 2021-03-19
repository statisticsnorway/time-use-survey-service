package no.ssb.timeuse.surveyservice.respondent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.ssb.timeuse.surveyservice.appointment.Appointment;
import no.ssb.timeuse.surveyservice.communicationlog.CommunicationLogEntry;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    // Personal information
    @Column(name = "NAME")
    private String name;

    @Column(name = "RESPONDENT_UUID")
    private UUID respondentId;

    @Column(name = "IO_NUMBER")
    private Long ioNumber;

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

    @Column(name = "EDUCATION")
    private String education;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "POSTCODE")
    private String postcode;

    @Column(name = "CITY")
    private String city;

    @Column(name = "REGION")
    private String region;

    @Column(name = "MUNICIPALITY_NUMBER")
    private String municipalityNumber;

    @Column(name = "DWELLING_NUMBER")
    private String dwellingNumber;


    // Survey information
    @Column(name = "DIARY_START")
    private LocalDate diaryStart;

    @Column(name = "DIARY_END")
    private LocalDate diaryEnd;

    @Column(name = "STATUS_DIARY")
    private String statusDiary;

    @Column(name = "STATUS_SURVEY")
    private String statusSurvey;

    @Column(name = "STATUS_RECRUITMENT")
    private String statusRecruitment;

    @Column(name = "STATUS_QUESTIONNAIRE")
    private String statusQuestionnaire;

    @Column(name = "RECRUITMENT_START")
    private LocalDateTime recruitmentStart;

    @Column(name = "RECRUITMENT_END")
    private LocalDateTime recruitmentEnd;

    @Column(name = "RECRUITMENT_MINUTES_SPENT")
    private Integer recruitmentMinutesSpent;

    @Column(name = "ACCEPTED_INITIAL_DIARY_START")
    private Boolean acceptedInitialDiaryStart;



    @OneToMany(mappedBy = "respondent", cascade = CascadeType.ALL)
    private List<CommunicationLogEntry> communicationLogs;

    @OneToMany(mappedBy = "respondent", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

}
