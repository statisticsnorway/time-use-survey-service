package no.ssb.timeuse.surveyservice.appointment;

import lombok.val;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.interviewer.InterviewerRepository;
import no.ssb.timeuse.surveyservice.respondent.RespondentRepository;
import no.ssb.timeuse.surveyservice.respondent.RespondentResponse;
import no.ssb.timeuse.surveyservice.respondent.RespondentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;
    RespondentRepository respondentRepository;
    InterviewerRepository interviewerRepository;


    public List<Appointment> getAppointmentsByIoNumber(Long ioNumber) {
        List<Appointment> appointmentList = appointmentRepository.findByRespondentIoNumber(ioNumber);

        return appointmentList;
    }

    public Appointment map(AppointmentRequest request) {
        val respondent = respondentRepository.findByRespondentId(request.getRespondentId())
                .orElseThrow(() -> new ResourceNotFoundException("Respondent with ID " + request.getRespondentId() + " does not exist"));
        val interviewer = request.getInterviewerId() != null ?
                interviewerRepository.findByInterviewerId(request.getInterviewerId())
                .orElseThrow(() -> new ResourceNotFoundException("Interviewer with ID " + request.getInterviewerId() + " does not exist"))
                : null;

        return Appointment.builder()
                .id(1L)
                .respondent(respondent)
                .interviewer(interviewer)
                .appointmentTime(request.getAppointmentTime())
                .createdBy(request.getCreatedBy())
                .description(request.getDescription())
                .build();

    }
}
