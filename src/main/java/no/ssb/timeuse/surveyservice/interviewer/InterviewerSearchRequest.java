package no.ssb.timeuse.surveyservice.interviewer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterviewerSearchRequest {
    String telephone;
    String initials;
    String name;
}
