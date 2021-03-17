package no.ssb.timeuse.surveyservice.household;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HouseholdRepository extends JpaRepository<Household, Long> {

    @Query("select h.statusSurvey, count(h) from Household h group by h.statusSurvey")
    List<Object[]> getNumberOfHousehouldsPerStatusSurvey();

    @Query("select h.statusSurvey, h.diaryStart, h.region, h.householdType, h.householdSize, count(h) from Household h group by h.statusSurvey, h.diaryStart, h.region, h.householdType, h.householdSize")
    List<Object[]> getNumberOfHousehouldsPerAlot();

    Optional<Household> findByIoNumber(Long ioNumber);

}

