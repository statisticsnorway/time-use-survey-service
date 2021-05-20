package no.ssb.timeuse.surveyservice.respondent.diarystarthistory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DIARY_START_HISTORY")
public class DiaryStartHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "diarystarthist_seq_generator")
    @SequenceGenerator(name = "diarystarthist_seq_generator", sequenceName = "DIARYSTARTHIST_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "RESPONDENT_ID", nullable = false)
    private Respondent respondent;

    @Column(name = "DIARY_START")
    private LocalDate diaryStart;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_TIME")
    private LocalDateTime createdTime;

    @Override
    public String toString() {
        return "DiaryStartHistory{" +
                "id=" + id +
                ", respondent=" + (respondent != null ? respondent.getRespondentId() : null) +
                ", diaryStart=" + diaryStart +
                ", createdBy='" + createdBy + '\'' +
                ", createdTime=" + createdTime +
                '}';
    }
}

