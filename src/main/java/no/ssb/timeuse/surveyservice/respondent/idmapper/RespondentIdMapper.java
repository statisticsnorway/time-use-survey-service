package no.ssb.timeuse.surveyservice.respondent.idmapper;

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
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RESPONDENT_MAPPER")
public class RespondentIdMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "respondent_mapper_seq_generator")
    @SequenceGenerator(name="respondent_mapper_seq_generator", sequenceName="RESPONDENT_MAPPER_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "RESPONDENT_UUID")
    private UUID respondentId;

    @Column(name = "FNR")
    private String personIdentificator;
}

