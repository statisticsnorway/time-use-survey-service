package no.ssb.timeuse.surveyservice.diary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryActivityRequest {
    private String startTime;
    private String endTime;
    private String activity;
    private String activityCode;
    private String biActivity;
    private String biActivityCode;
    private String travelType;
    private String company;
    private String createdTime;
}
