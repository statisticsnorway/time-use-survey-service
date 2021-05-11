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
    List<Respondent> findByName(String name);
    Optional<Respondent> findByIoNumber(Long ioNumber);
    Optional<Respondent> findByRespondentId(UUID respondentId);
    List<Respondent> findAllByRespondentIdIn(Set<UUID> respondentIds);

    void deleteById(Long id);
    void deleteRespondentByRespondentId(UUID respondentId);

    void deleteAllByIdIn(List<Long> id);
    List<Respondent> findAllByPhoneIgnoreCase(String phone);
    List<Respondent> findAllByEmailContainingIgnoreCase(String email);
    List<Respondent> findAllByNameContainingIgnoreCase(String name);


    @Query("select r.diaryStart as diaryStart, r.statusDiary as statusDiary, r.statusSurvey as statusSurvey, "
            + "r.statusRecruitment as statusRecruitment, r.statusQuestionnaire as statusQuestionnaire, count(r) as total "
            + "from Respondent r group by r.diaryStart, r.statusDiary, r.statusSurvey, r.statusRecruitment, r.statusQuestionnaire")
    List<RespondentMetricsStatusCount> getNumberOfRespondentsPerStatus();

    @Query("select extract(dow from r.diaryStart) as dayOfWeek, r.statusSurvey as statusSurvey, count(r) as total "
            + "from Respondent r group by dayOfWeek, statusSurvey")
    List<RespondentMetricsDayCount> getNumberOfRespondentsPerDiaryStartWeekday();

    @Query("select extract(dow from r.diaryEnd) as dayOfWeek, r.statusSurvey as statusSurvey, count(r) as total "
            + "from Respondent r group by dayOfWeek, statusSurvey")
    List<RespondentMetricsDayCount> getNumberOfRespondentsPerDiaryEndWeekday();

}
