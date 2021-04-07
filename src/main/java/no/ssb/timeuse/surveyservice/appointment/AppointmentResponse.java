package no.ssb.timeuse.surveyservice.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
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
//        log.info("appointment: {}", from);
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

