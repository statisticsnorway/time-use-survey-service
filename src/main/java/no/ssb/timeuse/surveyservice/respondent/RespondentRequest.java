package no.ssb.timeuse.surveyservice.respondent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespondentRequest {
    private Long ioNumber;
    private String name;
    private String phone;
    private String email;
    private String dateOfBirth;
    private Integer age;
    private String gender;
    private String education;
    private String address;
    private String postcode;
    private String city;
    private String region;
    private String municipalityNumber;
    private String dwellingNumber;
    private UUID interviewerId;

    private LocalDate diaryStart;
    private LocalDate diaryEnd;
    private LocalDate diaryStartOrig;
    private String statusDiary;
    private String statusSurvey;
    private String statusRecruitment;
    private String statusQuestionnaire;
    private LocalDateTime recruitmentStart;
    private LocalDateTime recruitmentEnd;
    private Integer recruitmentMinutesSpent;
    private Boolean acceptedInitialDiaryStart;

}
