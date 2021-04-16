package no.ssb.timeuse.surveyservice.utils.sample;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SampleImportInterviewer {

    private String id;
    private String initials;
    private String name;
    private String phone;
    private UUID newUUID;


}
