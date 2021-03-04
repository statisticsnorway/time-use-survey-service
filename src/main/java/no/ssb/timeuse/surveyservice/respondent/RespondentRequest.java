package no.ssb.timeuse.surveyservice.respondent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespondentRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Integer householdSequenceNumber;
    private Integer age;
    private String relationToRecruitmentRefPerson;
}
