package no.ssb.timeuse.surveyservice.interviewer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.ssb.timeuse.surveyservice.respondent.Respondent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterviewerResponse {
    UUID interviewerId;
    String name;
    String initials;
    String phone;
    List<String> respondents;

    public static InterviewerResponse map(Interviewer from) {
        InterviewerResponse response = InterviewerResponse.builder()
                .interviewerId(from.getInterviewerId())
                .name(from.getName())
                .build();
        log.info("response buildt: {}", response);
        return response;
    }
}
