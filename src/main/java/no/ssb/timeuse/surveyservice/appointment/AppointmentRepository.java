package no.ssb.timeuse.surveyservice.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByAssignedTo(String assignee);
    List<Appointment> findByRespondentIoNumber(Long ioNumber);
    List<Appointment> findByRespondentRespondentId(UUID respondentId);
}
