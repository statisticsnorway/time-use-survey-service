package no.ssb.timeuse.surveyservice.searchterm;

import lombok.AllArgsConstructor;
import lombok.val;
import no.ssb.timeuse.surveyservice.activitiy.ActivityCategoryRepository;
import no.ssb.timeuse.surveyservice.exception.ResourceExistsException;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.exception.ResourceValidationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class SearchTermService {
    private final SearchTermRepository repository;
    private final ActivityCategoryRepository activityCategoryRepository;

    public SearchTerm save(SearchTermRequest request, Optional<Long> id) {
        validateActivityCode(request.getActivityCode());
        findDuplicates(request);

        return repository.save(mapToDao(request, id));
    }

    public void validateActivityCode(String activityCode) {
        if (activityCategoryRepository.findByCode(activityCode).isEmpty()) {
            throw new ResourceValidationException("Activity Category with the code " + activityCode + " does not exist");
        }
    }

    public void findDuplicates(SearchTermRequest request) {
        if(repository.findByTextIgnoreCase(request.getText()).stream()
                .map(SearchTerm::getActivity)
                .anyMatch(d -> d.getCode().equals(request.getActivityCode()))) {
            throw new ResourceExistsException("A search term with the exact same text and ActivityCategory already exists!");
        }
    }

    public SearchTerm mapToDao(SearchTermRequest request, Optional<Long> searchTermId) {
        val linkedActivity = activityCategoryRepository.findByCode(request.getActivityCode());

        val newSearchTerm = SearchTerm.builder()
                .text(request.getText())
                .activity(linkedActivity.get())
                .build();

        if (searchTermId.isPresent()) {
            repository.findById(searchTermId.get())
                    .orElseThrow(() -> new ResourceNotFoundException("Search term with ID " + searchTermId.get() + " does not exist"));
            newSearchTerm.setId(searchTermId.get());
        }

            return newSearchTerm;
    }
}
