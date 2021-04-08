package no.ssb.timeuse.surveyservice.respondent.status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespondentStatus {

    private UUID respondentId;
    private String statusDiary;
    private String statusQuestionnaire;
}
