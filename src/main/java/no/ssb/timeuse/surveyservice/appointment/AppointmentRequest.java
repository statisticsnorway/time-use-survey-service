package no.ssb.timeuse.surveyservice.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentRequest {
    //private Long householdNumber;
    private UUID respondentId;
    private LocalDateTime appointmentTime;
    private String description;
    private String assignedTo;
    private String createdBy;

}
