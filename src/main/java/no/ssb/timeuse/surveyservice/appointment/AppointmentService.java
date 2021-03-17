package no.ssb.timeuse.surveyservice.appointment;

import lombok.val;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.respondent.RespondentRepository;
import no.ssb.timeuse.surveyservice.respondent.RespondentResponse;
import no.ssb.timeuse.surveyservice.respondent.RespondentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    AppointmentRepository repository;

    RespondentRepository respondentRepository;

    RespondentResponse respondentResponse;

    RespondentService respondentService;


    public List<Appointment> getAppointmentsByioNumber(Long ioNumber) {
        List<Appointment> appointmentList = repository.findByRespondentHouseholdIoNumber(ioNumber);

        return appointmentList;
    }

    public Appointment map(AppointmentRequest request) {
        val respondent = respondentRepository.findByRespondentId(request.getRespondentId())
                .orElseThrow(() -> new ResourceNotFoundException("Respondent with ID " + request.getRespondentId() + " does not exist"));

        return Appointment.builder()
                .id(1L)
                .respondent(respondent)
                .assignedTo(request.getAssignedTo())
                .appointmentTime(request.getAppointmentTime())
                .createdBy(request.getCreatedBy())
                .description(request.getDescription())
                .build();

    }
}
