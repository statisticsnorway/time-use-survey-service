package no.ssb.timeuse.surveyservice.searchterm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.ssb.timeuse.surveyservice.activitiy.ActivityCategory;

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

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "SEARCH_TERM")
public class SearchTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "search_term_seq_generator")
    @SequenceGenerator(name="search_term_seq_generator", sequenceName="SEARCH_TERM_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "TEXT", nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACTIVITY_CATEGORY_ID")
    private ActivityCategory activity;
}
