package no.ssb.timeuse.surveyservice.communicationlog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface CommunicationLogRepository extends JpaRepository<CommunicationLogEntry, Long> {
    List<CommunicationLogEntry> findByRespondentRespondentId(UUID respondentId);

    List<CommunicationLogEntry> findByConfirmedSent(LocalDateTime sent);

    @Query("select c.category, count(c) from CommunicationLogEntry c group by c.category")
    List<Object[]> getNumberOfCommLogEntriesPerCategory();

    List<CommunicationLogEntry> findAllByConfirmedSentIsNull();
    List<CommunicationLogEntry> findAllByIdIn(List<Long> ids);
}
