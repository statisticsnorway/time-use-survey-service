package no.ssb.timeuse.surveyservice.respondent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespondentResponse {
    UUID respondentId;
    Long ioNumber;
    String name;
    String phone;
    String email;
    String gender;
    LocalDate dateOfBirth;
    Integer age;
    String education;
    String address;
    String postcode;
    String city;
    String region;
    String municipalityNumber;
    String dwellingNumber;
    String interviewerId;
    String interviewerName;
    String interviewerInitials;
    String interviewerPhone;

    LocalDate diaryStart;
    LocalDate diaryEnd;
    String statusDiary;
    String statusSurvey;
    String statusRecruitment;
    String statusQuestionnaire;
    LocalDateTime recruitmentStart;
    LocalDateTime recruitmentEnd;
    Integer recruitmentMinutesSpent;
    Boolean acceptedInitialDiaryStart;

    public static RespondentResponse map(Respondent from) {
        RespondentResponse response = RespondentResponse.builder()
                .respondentId(from.getRespondentId())
                .name(from.getName())
                .gender(from.getGender())
                .dateOfBirth(from.getDateOfBirth())
                .phone(from.getPhone())
                .email(from.getEmail())
                .ioNumber(from.getIoNumber())
                .age(from.getAge())
                .education(from.getEducation())
                .address(from.getAddress())
                .postcode(from.getPostcode())
                .city(from.getCity())
                .region(from.getRegion())
                .municipalityNumber(from.getMunicipalityNumber())
                .dwellingNumber(from.getDwellingNumber())
                .interviewerId(from.getInterviewer() != null ? from.getInterviewer().getInterviewerId().toString() : null)
                .interviewerName(from.getInterviewer() != null ? from.getInterviewer().getName(): null)
                .interviewerInitials(from.getInterviewer() != null ? from.getInterviewer().getInitials(): null)
                .interviewerPhone(from.getInterviewer() != null ? from.getInterviewer().getPhone(): null)
                .diaryStart(from.getDiaryStart())
                .diaryEnd(from.getDiaryEnd())
                .statusDiary(from.getStatusDiary())
                .statusSurvey(from.getStatusSurvey())
                .statusRecruitment(from.getStatusRecruitment())
                .statusQuestionnaire(from.getStatusQuestionnaire())
                .recruitmentStart(from.getRecruitmentStart())
                .recruitmentEnd(from.getRecruitmentEnd())
                .recruitmentMinutesSpent(from.getRecruitmentMinutesSpent())
                .acceptedInitialDiaryStart(from.getAcceptedInitialDiaryStart())
                .build();
        log.info("response buildt: {}", response);
        return response;
    }
}
