package no.ssb.timeuse.surveyservice.appointment;

import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.val;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.exception.ResourceValidationException;
import no.ssb.timeuse.surveyservice.respondent.RespondentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Timed
@RequestMapping("/v1/appointments")
public class AppointmentController {

    private final AppointmentRepository repository;
    private final AppointmentService service;
    private final RespondentRepository respondentRepository;



    @CrossOrigin
    @GetMapping
    public List<AppointmentResponse> entries(@RequestParam(required = false) Optional<Long> ioNumber,
                                             @RequestParam(required = false) Optional<UUID> respondentId,
                                             @RequestParam(required = false) Optional<String> assignedTo) {
       if (respondentId.isPresent()) {
            return repository.findByRespondentRespondentId(respondentId.get())
                    .stream()
                    .map(r -> AppointmentResponse.map(r))
                    .collect(Collectors.toList());
        }
        if (ioNumber.isPresent()) {
            return repository.findByRespondentHouseholdIoNumber(ioNumber.get())
                    .stream()
                    .map(r -> AppointmentResponse.map(r))
                    .collect(Collectors.toList());
        }
        if (assignedTo.isPresent()) {
            return repository.findByAssignedTo(assignedTo.get())
                    .stream()
                    .map(r -> AppointmentResponse.map(r))
                    .collect(Collectors.toList());
        }
        return repository.findAll()
                .stream()
                .map(r -> AppointmentResponse.map(r))
                .collect(Collectors.toList());
    }



    @CrossOrigin
    @GetMapping("/{id}")
    public AppointmentResponse appointmentById(@PathVariable Long id) {
        return repository.findById(id)
                .map(AppointmentResponse::map)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with id " + id + " does not exist"));
    }



    @CrossOrigin
    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Appointment with id " + id + " does not exist");
        }
    }


    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponse> updateAppointment(@PathVariable(value = "id") Long id, @RequestBody AppointmentRequest request) {
        if (repository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Appointment with id " + id + " does not exist");
        }
        if (respondentRepository.findByRespondentId(request.getRespondentId()).isEmpty()) {
            throw new ResourceValidationException("Respondent with ioIdNumber " + request.getRespondentId() + " does not exist");
        }
        return new ResponseEntity<>(AppointmentResponse.map(repository.save(convertToAppointment(Optional.of(id), request))), HttpStatus.OK);
    }


    @CrossOrigin
    @PostMapping
    public ResponseEntity<?> createNewAppointment(@RequestBody AppointmentRequest request) {
        if (respondentRepository.findByRespondentId(request.getRespondentId()).isEmpty()) {
            throw new ResourceValidationException("Respondent with respondentId " + request.getRespondentId() + " does not exist");
        }
        Appointment p = convertToAppointment(Optional.empty(), request);
        return new ResponseEntity<>(AppointmentResponse.map(repository.save(p)), HttpStatus.CREATED);
    }

    final Appointment convertToAppointment(Optional<Long> id, AppointmentRequest request) {
        val respondent = respondentRepository.findByRespondentId(request.getRespondentId());

        Appointment appointment = Appointment.builder()
                .respondent(respondent.get())
                .appointmentTime(request.getAppointmentTime())
                .assignedTo(request.getAssignedTo())
                .createdBy(request.getCreatedBy())
                .description(request.getDescription())
                .build();

        if (id.isPresent()) {
            appointment.setId(id.get());
        }

        return appointment;
    }

}



