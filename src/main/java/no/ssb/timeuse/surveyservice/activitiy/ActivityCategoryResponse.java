package no.ssb.timeuse.surveyservice.activitiy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityCategoryResponse {
     Long id;
     String description;
     String code;
     Integer level;
     String helpText;

    public static ActivityCategoryResponse map(ActivityCategory from) {
        return new ActivityCategoryResponse(from.getId(), from.getDescription(), from.getCode(), from.getLevel(), from.getHelpText());
    }

}
