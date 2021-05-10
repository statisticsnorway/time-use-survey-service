package no.ssb.timeuse.surveyservice.templates;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
    Optional<Template> findById(Long id);
    Optional<Template> findByName(String name);
    List<Template> findAllByNameContainingIgnoreCase(String name);
    List<Template> findAllByType(String type);
}
