package no.ssb.timeuse.surveyservice.interviewer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterviewerRequest {
    private String name;
    private String initials;
    private String phone;
    private List<String> respondents;
}
