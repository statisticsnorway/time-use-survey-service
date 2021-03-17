package no.ssb.timeuse.surveyservice.activitiy;

import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import no.ssb.timeuse.surveyservice.exception.*;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
@Timed
@RequestMapping("/v1/activity-categories")
public class ActivityCategoryController {

    private final ActivityCategoryRepository repository;
    private final ActivityCategoryService activityCategoryService;

    @CrossOrigin
    @GetMapping
    public List<ActivityCategoryResponse> entries() {
        return repository.findAll().stream()
                .map(ActivityCategoryResponse::map)
                .collect(Collectors.toList());
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<ActivityCategory> createNewActivity(@RequestBody ActivityCategory activity) {
        activityCategoryService.checkForActivityDuplicate(activity);
        activityCategoryService.validateActivity(activity);
        return new ResponseEntity<>(repository.save(activity), HttpStatus.CREATED);
    }

    @CrossOrigin
    @PutMapping("/{code}")
    public ActivityCategoryResponse updateActivity(@PathVariable(value = "code") String code, @RequestBody ActivityCategoryRequest activityRequest) {
        var updatedactivity = repository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Activity Category with code " + code + " does not exist"));

        if (repository.findByDescription(activityRequest.getDescription()).isPresent()) {
            throw new ResourceExistsException("Activity Category must be unique: Description: " + activityRequest.getDescription() + " already exists");
        }

        updatedactivity = ActivityCategory.builder()
                .code(code)
                .description(activityRequest.getDescription())
                .id(updatedactivity.getId())
                .helpText(activityRequest.getHelpText())
                .build();

        activityCategoryService.validateActivity(updatedactivity);
        return ActivityCategoryResponse.map(repository.save(updatedactivity));
    }

    @CrossOrigin
    @DeleteMapping("/{code}")
    public void deleteActivity(@PathVariable String code) {
        throw new MethodNotAllowedException("Delete methode is not allowed for Activity Category");
    }
}
