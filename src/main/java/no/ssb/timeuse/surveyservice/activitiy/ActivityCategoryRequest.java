package no.ssb.timeuse.surveyservice.activitiy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityCategoryRequest {
    private String description;
    private String helpText;
}
