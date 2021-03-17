package no.ssb.timeuse.surveyservice.searchterm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchTermRepository extends JpaRepository<SearchTerm, Long> {
    List<SearchTerm> findByTextStartsWithIgnoreCase(String text);
    List<SearchTerm> findByTextIgnoreCase(String text);
    List<SearchTerm> findByActivityCode(String code);
}
