package no.ssb.timeuse.surveyservice.respondent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespondentResponse {
    String firstName;
    String lastName;
    String phone;
    String email;
    String gender;
    LocalDate dateOfBirth;
    UUID respondentId;
    Long iONumber;
    Integer householdSequenceNumber;
    Integer age;
    String relationToRecruitmentRefPerson;

    public static RespondentResponse map(Respondent from) {
        RespondentResponse response = RespondentResponse.builder()
                .respondentId(from.getRespondentId())
                .firstName(from.getFirstName())
                .lastName(from.getLastName())
                .gender(from.getGender())
                .dateOfBirth(from.getDateOfBirth())
                .householdSequenceNumber(from.getHouseholdSequenceNumber())
                .phone(from.getPhone())
                .email(from.getEmail())
                .iONumber(from.getHousehold().getIoNumber())
                .age(from.getAge())
                .relationToRecruitmentRefPerson(from.getRelationToRecruitmentRefPerson())
                .build();
        return response;
    }
}
