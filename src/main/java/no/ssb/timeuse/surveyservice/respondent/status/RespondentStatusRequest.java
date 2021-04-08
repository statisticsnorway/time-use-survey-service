package no.ssb.timeuse.surveyservice.respondent.status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespondentStatusRequest {
    private UUID respondentId;
    private String statusDiary;
    private String statusQuestionnaire;

}
