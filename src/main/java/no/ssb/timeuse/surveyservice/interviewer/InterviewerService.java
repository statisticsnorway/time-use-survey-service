package no.ssb.timeuse.surveyservice.interviewer;

import lombok.AllArgsConstructor;
import lombok.val;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.exception.ResourceValidationException;
import no.ssb.timeuse.surveyservice.utils.sample.SampleImportInterviewer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class InterviewerService {

    private final InterviewerRepository interviewerRepository;

    public void validateInterviewer(InterviewerRequest request) {
        if (request.getName().isEmpty()) {
            throw new ResourceValidationException("interviewer must have a name");
        }
    }

    public Interviewer mapToDao(InterviewerRequest request, Optional<UUID> interviewerId) {
        validateInterviewer(request);

        val newInterviewer = Interviewer.builder()
                .name(request.getName())
                .initials(request.getInitials())
                .phone(request.getPhone())
                .build();

        if (interviewerId.isPresent()) {
            val existingInterviewer = interviewerRepository.findByInterviewerId(interviewerId.get())
                    .orElseThrow(() -> new ResourceNotFoundException("interviewer with interviewerId " + interviewerId.get() + " does not exist"));
            newInterviewer.setInterviewerId(existingInterviewer.getInterviewerId());
            newInterviewer.setId(existingInterviewer.getId());
        }
        return newInterviewer;
    }


    public List<InterviewerResponse> searchinterviewer(InterviewerSearchRequest searchRequest) {
        if (searchRequest.telephone != null) {
            return interviewerRepository.findAllByPhoneIgnoreCase(searchRequest.getTelephone())
                    .stream().map(InterviewerResponse::map)
                    .collect(Collectors.toList());
        } else if (searchRequest.name != null) {
            return interviewerRepository.findAllByNameContainingIgnoreCase(searchRequest.getName())
                    .stream().map((InterviewerResponse::map))
                    .collect(Collectors.toList());
        }

        return interviewerRepository.findAllByInitialsContainingIgnoreCase(searchRequest.getInitials())
                .stream().map((InterviewerResponse::map))
                .collect(Collectors.toList());

    }

    public Interviewer mapToInterviewerFromSample(SampleImportInterviewer request) {

        return Interviewer.builder()
                .interviewerId(request.getNewUUID())
                .initials(request.getInitials())
                .name(request.getName())
                .phone(request.getPhone())
                .build();
    }

}
