package no.ssb.timeuse.surveyservice.activitiy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ACTIVITY_CATEGORY")
public class ActivityCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_category_seq_generator")
    @SequenceGenerator(name="activity_category_seq_generator", sequenceName="ACTIVITY_CATEGORY_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "CODE", nullable = false, unique = true)
    private String code;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "LEVEL", nullable = false)
    private Integer level;

    @Column(name = "HELPTEXT")
    private String helpText;

    public static ActivityCategory map(ActivityCategoryRequest from) {
        return ActivityCategory.builder()
                .description(from.getDescription())
                .helpText(from.getHelpText())
                .build();
    }
}
