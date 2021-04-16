package no.ssb.timeuse.surveyservice.diary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DiaryRespondentResponse {
    UUID respondentId;
    List<DiaryActivityResponse> diaryActivities;

    public static DiaryRespondentResponse map(List<Diary> from) {
        return new DiaryRespondentResponse(
                from.get(0).getRespondent().getRespondentId(),
                from.stream()
                        .map(DiaryActivityResponse::map)
                        .collect(Collectors.toList())
        );
    }
}
