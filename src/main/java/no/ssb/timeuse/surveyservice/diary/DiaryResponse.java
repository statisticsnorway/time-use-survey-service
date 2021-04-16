package no.ssb.timeuse.surveyservice.diary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DiaryResponse {
    UUID respondentId;
    DiaryActivityResponse diaryActivityResponse;


    public static DiaryResponse map(Diary from) {
        return new DiaryResponse(
                from.getRespondent().getRespondentId(),
                DiaryActivityResponse.map(from)
        );
    }
}
