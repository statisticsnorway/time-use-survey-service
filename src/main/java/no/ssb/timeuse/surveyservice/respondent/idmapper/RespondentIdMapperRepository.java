package no.ssb.timeuse.surveyservice.respondent.idmapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespondentIdMapperRepository extends JpaRepository<RespondentIdMapper, Long> {

}
