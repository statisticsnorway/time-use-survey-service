package no.ssb.timeuse.surveyservice.respondent.diarystarthistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DiaryStartHistoryRepository extends JpaRepository<DiaryStartHistory, Long> {
    List<DiaryStartHistory> findByRespondentRespondentId(UUID respondentId);
}
