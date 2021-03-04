package no.ssb.timeuse.surveyservice.respondent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface RespondentRepository extends JpaRepository<Respondent, Long> {
    List<Respondent> findByLastName(String lastName);
    Optional<Respondent> findByRespondentId(UUID respondentId);
    List<Respondent> findAllByRespondentIdIn(Set<UUID> respondentIds);
    Optional<Respondent> findByPhone(String phone);
    Optional<Respondent> findByEmail(String email);
}
