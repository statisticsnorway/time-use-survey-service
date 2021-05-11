package no.ssb.timeuse.surveyservice.interviewer;

import io.netty.handler.codec.http2.Http2Connection;
import no.ssb.timeuse.surveyservice.respondent.Respondent;
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

    List<Interviewer> findAllByPhoneIgnoreCase(String phone);
    List<Interviewer> findAllByInitialsContainingIgnoreCase(String initials);
    List<Interviewer> findAllByNameContainingIgnoreCase(String name);
}
