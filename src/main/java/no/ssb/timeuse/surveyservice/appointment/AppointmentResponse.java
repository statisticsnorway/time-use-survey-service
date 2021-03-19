package no.ssb.timeuse.surveyservice.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {
    Long id;
    UUID respondentId;
    String name;
    LocalDateTime appointmentTime;
    String description;
    String assignedTo;
    String createdBy;

    public static AppointmentResponse map(Appointment from) {
        return new AppointmentResponse(
                from.getId(),
                from.getRespondent().getRespondentId(),
                from.getRespondent().getName(),
                from.getAppointmentTime(),
                from.getDescription(),
                from.getAssignedTo(),
                from.getCreatedBy());
    }


}

