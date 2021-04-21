package no.ssb.timeuse.surveyservice.diary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByRespondentId(UUID respondentId);
   void deleteByRespondentId(UUID respondentId);
}
