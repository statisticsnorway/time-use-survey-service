package no.ssb.timeuse.surveyservice.household;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "HOUSEHOLD")
public class Household {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "household_seq_generator")
    @SequenceGenerator(name="household_seq_generator", sequenceName="HOUSEHOLD_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "REFERENCE_WEEK")
    private Integer referenceWeek;

    @Column(name = "MAIN_RESPONDENT_UUID")
    private UUID mainRespondentId;

    @Column(name = "IO_NUMBER")
    private Long ioNumber;

    @Column(name = "STATUS_SURVEY")
    private String statusSurvey;

    @Column(name = "STATUS_RECRUITMENT")
    private String statusRecruitment;

    @Column(name = "STATUS_DIARY")
    private String statusDiary;

    @Column(name = "STATUS_QUESTIONNAIRE")
    private String statusQuestionnaire;

    @Column(name = "DIARY_START")
    private LocalDate diaryStart;

    @Column(name = "DIARY_END")
    private LocalDate diaryEnd;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "POSTCODE")
    private String postcode;

    @Column(name = "CITY")
    private String city;

    @Column(name = "RECRUITMENT_START")
    private LocalDateTime recruitmentStart;

    @Column(name = "RECRUITMENT_END")
    private LocalDateTime recruitmentEnd;

    @Column(name = "RECRUITMENT_MINUTES_SPENT")
    private Integer recruitmentMinutesSpent;

    @Column(name = "ACCEPTED_INITIAL_DIARY_START")
    private Boolean acceptedInitialDiaryStart;

    @Column(name = "HOUSEHOLD_SIZE")
    private Integer householdSize;

    @Column(name = "REGION")
    private String region;

    @Column(name = "MUNICIPALITY_NUMBER")
    private String municipalityNumber;

    @Column(name = "DWELLING_NUMBER")
    private String dwellingNumber;

    @Column(name = "HOUSEHOLD_TYPE")
    private String householdType;

    @OneToMany(mappedBy = "household", cascade = CascadeType.ALL)
    private List<Respondent> respondents;

}
