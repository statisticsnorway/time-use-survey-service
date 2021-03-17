package no.ssb.timeuse.surveyservice.activitiy;

import no.ssb.timeuse.surveyservice.exception.ResourceExistsException;
import no.ssb.timeuse.surveyservice.exception.ResourceValidationException;
import org.springframework.stereotype.Component;

@Component
public class ActivityCategoryService {

    private final ActivityCategoryRepository repository;

    public ActivityCategoryService(ActivityCategoryRepository repository) {
        this.repository = repository;
    }

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static final String VERSION_STRING_REGEX = "(\\d\\d(\\.\\d)?(\\.\\d)?(\\.\\d)?)";

    public static boolean isCode(String version){
        return version.matches(VERSION_STRING_REGEX);
    }

    public static boolean isDescription(String description) {
        return !description.isEmpty();
    }

    public void validateActivity(ActivityCategory activity) {
        activity.setLevel(calculateActivityLevel(activity.getCode()));
        if (!ActivityCategoryService.isDescription(activity.getDescription()) || !isCode(activity.getCode()) || activity.getLevel()>4) {
            throw new ResourceValidationException("Activity Category is invalid");
        }
    }

    public void checkForActivityDuplicate(ActivityCategory activity) {
        if (repository.findByDescription(activity.getDescription()).isPresent()){
            throw new ResourceExistsException("Activity Category must be unique: Description: " + activity.getDescription() + " already exists");
        } else if (repository.findByCode(activity.getCode()).isPresent()) {
            throw new ResourceExistsException("Activity Category must be unique: Code: " + activity.getCode() + " already exists");
        }
    }

    public Integer calculateActivityLevel(String code) {
        Integer count = -1;
        for (int i=0; i<code.length(); i++) {
            if(Character.isDigit(code.charAt(i))) count++;
        }
        return count;
    }

}
