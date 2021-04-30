package no.ssb.timeuse.surveyservice.communicationlog.contactio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ContactIoDto {
    private String requestId;
    private String type;
    private String replyTo;
    private String to;
    private String subject;
    private String message;
}
