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

    List<Respondent> findAllByInterviewerInterviewerId(UUID interviewerId);


    @Query("select r.diaryStart as diaryStart, r.statusDiary as statusDiary, r.statusSurvey as statusSurvey, "
            + "r.statusRecruitment as statusRecruitment, r.statusQuestionnaire as statusQuestionnaire, count(r) as total "
            + "from Respondent r group by r.diaryStart, r.statusDiary, r.statusSurvey, r.statusRecruitment, r.statusQuestionnaire")
    List<RespondentMetricsStatusCount> getNumberOfRespondentsPerStatus();


    // from forbruk 30.04.2020
    @Query("select r.statusSurvey as status, r.diaryStart as diaryStart, r.region as region , count(r) as total "
            + "from Respondent r group by r.statusSurvey, r.diaryStart, r.region")
    List<MetricsCountStatusByDiaryStart> getNumberOfRespondentsPerStatusSurvey();

    @Query("select r.statusRecruitment as status, r.diaryStart as diaryStart, r.region as region , count(r) as total "
            + "from Respondent r group by r.statusRecruitment, r.diaryStart, r.region")
    List<MetricsCountStatusByDiaryStart> getNumberOfRespondentsPerStatusRecruitment();

    @Query("select r.statusDiary as status, r.diaryStart as diaryStart, r.region as region , count(r) as total "
            + "from Respondent r group by r.statusDiary, r.diaryStart, r.region")
    List<MetricsCountStatusByDiaryStart> getNumberOfRespondentsPerStatusDiary();

    @Query("select r.statusQuestionnaire as status, r.diaryStart as diaryStart, r.region as region , count(r) as total "
            + "from Respondent r group by r.statusQuestionnaire, r.diaryStart, r.region")
    List<MetricsCountStatusByDiaryStart> getNumberOfRespondentsPerStatusQuestionnaire();


    @Query("select extract(year from r.diaryStart) as year, extract(month from r.diaryStart) as month, extract(dow from r.diaryStart) as dayOfWeek, r.statusSurvey as statusSurvey, count(r) as total "
            + "from Respondent r group by year, month, dayOfWeek, statusSurvey")
    List<RespondentMetricsDayCount> getNumberOfRespondentsPerDiaryStartWeekday();

    @Query("select extract(year from r.diaryEnd) as year, extract(month from r.diaryEnd) as month, extract(dow from r.diaryEnd) as dayOfWeek, r.statusSurvey as statusSurvey, count(r) as total "
            + "from Respondent r group by year, month, dayOfWeek, statusSurvey")
    List<RespondentMetricsDayCount> getNumberOfRespondentsPerDiaryEndWeekday();

}
