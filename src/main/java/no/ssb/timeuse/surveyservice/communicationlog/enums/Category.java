package no.ssb.timeuse.surveyservice.communicationlog.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Category {
    INVITATION,
    REMINDER,
    APPOINTMENT,
    HELPDESK,
    FOLLOWUP,
    INTERNAL_MESSAGE,
    FOLLOWUP_RESPONSE,
    FOLLOWUP_NO_RESPONSE
}
