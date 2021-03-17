package no.ssb.timeuse.surveyservice.activitiy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityCategoryRepository extends JpaRepository<ActivityCategory, Long> {
    Optional<ActivityCategory> findByDescription (String description);
    List<ActivityCategory> findByLevel (Integer level);
    Optional<ActivityCategory> findByCode (String code);
}
