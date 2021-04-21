package no.ssb.timeuse.surveyservice.interviewer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface InterviewerRepository extends JpaRepository<Interviewer, Long> {
    List<Interviewer> findByName(String name);
    Optional<Interviewer> findByInitials(String initials);
    Optional<Interviewer> findByInterviewerId(UUID interviewerId);
    List<Interviewer> findAllByInterviewerIdIn(Set<UUID> interviewerIds);

    void deleteById(Long id);

    void deleteAllByIdIn(List<Long> id);

}
