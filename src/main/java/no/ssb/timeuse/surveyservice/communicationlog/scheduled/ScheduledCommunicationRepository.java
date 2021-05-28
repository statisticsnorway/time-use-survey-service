package no.ssb.timeuse.surveyservice.communicationlog.scheduled;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface ScheduledCommunicationRepository extends JpaRepository<ScheduledCommunication, Long> {
    List<ScheduledCommunication> findAllByConfirmedSentIsNullAndScheduledIsBefore(LocalDateTime now);
    List<ScheduledCommunication> findAllByConfirmedSentIsNull();
    List<ScheduledCommunication> findByConfirmedSent(LocalDateTime sent);


    @Query("select c.category, count(c) from ScheduledCommunication c group by c.category")
    List<Object[]> getNumberOfSchedCommPerCategory();


}
