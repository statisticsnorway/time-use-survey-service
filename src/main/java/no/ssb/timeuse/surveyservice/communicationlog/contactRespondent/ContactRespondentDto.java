package no.ssb.timeuse.surveyservice.communicationlog.contactRespondent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ContactRespondentDto {
    private String requestId;
    private String type;
    private String replyTo;
    private String to;
    private String subject;
    private String message;
}
