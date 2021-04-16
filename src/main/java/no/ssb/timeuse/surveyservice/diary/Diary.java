package no.ssb.timeuse.surveyservice.diary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.ssb.timeuse.surveyservice.activitiy.ActivityCategory;
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
@Table(name = "DIARY")
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "diary_seq_generator")
    @SequenceGenerator(name="diary_seq_generator", sequenceName="DIARY_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "RESPONDENT_ID", nullable = false)
    private Respondent respondent; //(FK)

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @Column(name = "ACTIVITY")
    private String activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACTIVITY_CATEGORY")
    private ActivityCategory activityCategory;

    @Column(name = "BI_ACTIVITY")
    private String biActivity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BI_ACTIVITY_CATEGORY")
    private ActivityCategory biActivityCategory;

    @Column(name = "TRAVEL_TYPE")
    private String travelType;

    @Column(name = "COMPANY")
    private String company;


    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "MODIFIED_DATE")
    private LocalDateTime modifiedDate;
}
