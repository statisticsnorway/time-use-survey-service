package no.ssb.timeuse.surveyservice.diary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryActivityResponse {
    Long id;
    String activity;
    String activityCode;
    String biActivity;
    String biActivityCode;
    String travelType;
    String company;

    public static DiaryActivityResponse map (Diary from) {
        DiaryActivityResponse diaryActivityResponse = DiaryActivityResponse.builder()
                .id(from.getId())
                .activity(from.getActivity())
                .activityCode(from.getActivityCategory() != null ? from.getActivityCategory().getCode() : null)
                .biActivity(from.getBiActivity())
                .biActivityCode(from.getBiActivityCategory() != null ? from.getBiActivityCategory().getCode() : null)
                .travelType(from.getTravelType())
                .company(from.getCompany())
                .build();

        return diaryActivityResponse;
    }

}
