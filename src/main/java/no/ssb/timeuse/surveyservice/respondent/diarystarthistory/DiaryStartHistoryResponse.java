package no.ssb.timeuse.surveyservice.respondent.diarystarthistory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiaryStartHistoryResponse {
    Long id;
    UUID respondentId;
    LocalDate diaryStart;
    String createdBy;
    LocalDateTime createdTime;

    public static DiaryStartHistoryResponse map(DiaryStartHistory from) {
        log.info("diaryStartHistory.respondentId: {}", from.getRespondent().getRespondentId());
        return new DiaryStartHistoryResponse(
                from.getId(),
                from.getRespondent().getRespondentId(),
                from.getDiaryStart(),
                from.getCreatedBy(),
                from.getCreatedTime());

    }


}

