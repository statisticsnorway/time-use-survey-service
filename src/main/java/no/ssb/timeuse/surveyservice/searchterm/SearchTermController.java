package no.ssb.timeuse.surveyservice.searchterm;


import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.val;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
@Timed
@RequestMapping("/v1/search-terms")
public class SearchTermController {

    private final SearchTermRepository repository;
    private final SearchTermService service;

    @CrossOrigin
    @GetMapping
    public List<SearchTermResponse> entries(@RequestParam(required = false) Optional<String> text, @RequestParam(required = false) Optional<String> activityCode) {
        if(activityCode.isPresent()) {
            return repository.findByActivityCode(activityCode.get()).stream()
                    .filter(searchTerm -> searchTerm.getText().toLowerCase().contains(text.orElse("").toLowerCase()))
                    .map(SearchTermResponse::map)
                    .collect(Collectors.toList());
        } else {
            return repository.findByTextStartsWithIgnoreCase(text.orElse("")).stream()
                    .map(SearchTermResponse::map)
                    .collect(Collectors.toList());
        }
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public SearchTermResponse searchById(@PathVariable Long id) {
        return repository.findById(id)
                .map(SearchTermResponse::map)
                .orElseThrow(() -> new ResourceNotFoundException("Search term with id " + id + " does not exist"));
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public SearchTermResponse updateSearchTerm(@PathVariable(value = "id") Long id, @RequestBody SearchTermRequest searchTermRequest) {
        return SearchTermResponse.map(service.save(searchTermRequest, Optional.of(id)));
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public void deleteSearchTerm(@PathVariable Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<SearchTermResponse> createNewSearchTerm(@RequestBody SearchTermRequest searchTermRequest) {
        return new ResponseEntity<>(SearchTermResponse.map(service.save(searchTermRequest, Optional.empty())), HttpStatus.CREATED);
    }

    @CrossOrigin
    @PostMapping("/create-batch")
    public ResponseEntity<List<SearchTermResponse>> createNewSearchTermBatch(@RequestBody List<SearchTermRequest> searchTermRequest) {
        val searchTerms = new ArrayList<SearchTerm>();

        searchTermRequest.forEach(r -> {
            service.validateActivityCode(r.getActivityCode());
            service.findDuplicates(r);
            searchTerms.add(service.mapToDao(r, Optional.empty()));
        });

        return new ResponseEntity<>(repository.saveAll(searchTerms).stream()
                .map(SearchTermResponse::map)
                .collect(Collectors.toList()), HttpStatus.CREATED);
    }
}



