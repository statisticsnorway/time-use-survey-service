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
public class SampleImport {

    private String personIdentificator;
    private String ioNumber;
    private String name;
    private String careOfAddress;
    private String address;
    private String dwellingNumber;
    private String postCode;
    private String city;
    private String municipalityNumber;
    private String region;
    private String dateOfBirth;
    private Integer age;
    private String gender;
    private String education;
    private String phone;
    private String email;
    private UUID newUUID;
    private String statusRecruitment = "NOT_STARTED";
    private String statusQuestionnaire = "NOT_STARTED";
    private String statusDiary = "NOT_STARTED";
    private String statusSurvey = "NOT_STARTED";
    private String diaryStart;
    private String diaryEnd;


}
