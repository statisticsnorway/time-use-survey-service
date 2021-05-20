package no.ssb.timeuse.surveyservice.respondent.diarystarthistory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DiaryStartHistoryRequest {
    private UUID respondentId;
    private LocalDate diaryStart;
    private String createdBy;
    private LocalDateTime createdTime;
}
