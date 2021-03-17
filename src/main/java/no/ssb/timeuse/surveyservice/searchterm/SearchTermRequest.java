package no.ssb.timeuse.surveyservice.searchterm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchTermRequest {
    private String text;
    private String activityCode;
}
