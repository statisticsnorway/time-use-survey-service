package no.ssb.timeuse.surveyservice.respondent;

import no.ssb.timeuse.surveyservice.search.RespondentSearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface RespondentRepository extends JpaRepository<Respondent, Long>, RespondentSearchRepository {
    @Query("select h.statusSurvey, h.diaryStart, h.region, count(h) " +
            "from Respondent h group by h.statusSurvey, h.diaryStart, h.region")
    List<Object[]> getNumberOfRespondentsPerStatusSurvey();

    @Query("select h.statusRecruitment, h.diaryStart, h.region, count(h) " +
            "from Respondent h group by h.statusRecruitment, h.diaryStart, h.region")
    List<Object[]> getNumberOfRespondentsPerStatusRecruitment();

    @Query("select h.statusDiary, h.diaryStart, h.region, count(h) " +
            "from Respondent h group by h.statusDiary, h.diaryStart, h.region")
    List<Object[]> getNumberOfRespondentsPerStatusDiary();

    @Query("select h.statusQuestionnaire, h.diaryStart, h.region, count(h) " +
            "from Respondent h group by h.statusQuestionnaire, h.diaryStart, h.region")
    List<Object[]> getNumberOfRespondentsPerStatusQuestionnaire();

    List<Respondent> findByName(String name);
    Optional<Respondent> findByIoNumber(Long ioNumber);
    Optional<Respondent> findByRespondentId(UUID respondentId);
    List<Respondent> findAllByRespondentIdIn(Set<UUID> respondentIds);
    Optional<Respondent> findByPhone(String phone);
    Optional<Respondent> findByEmail(String email);
}
