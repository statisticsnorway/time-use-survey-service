package no.ssb.timeuse.surveyservice.interviewer;

import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.search.SearchRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@CrossOrigin
@RestController
@Slf4j
@AllArgsConstructor
@Timed
@RequestMapping("/v1/interviewers")
public class InterviewerController {

    private final InterviewerRepository interviewerRepository;
    private final InterviewerService interviewerService;

    @GetMapping
    public List<InterviewerResponse> entries() {
        return interviewerRepository.findAll().stream()
                .map(r -> InterviewerResponse.map(r))
                .collect(Collectors.toList());
    }

    @GetMapping("{interviewerId}")
    public InterviewerResponse findInterviewerByInterviewerId(@PathVariable UUID interviewerId) {
        return interviewerRepository.findByInterviewerId(interviewerId)
                .map(r -> InterviewerResponse.map(r))
                .orElseThrow(() -> new ResourceNotFoundException("interviewer with interviewerId " + interviewerId + " does not exist"));
    }

    @PutMapping("{interviewerId}")
    public InterviewerResponse updateInterviewer(@PathVariable UUID interviewerId, @RequestBody InterviewerRequest interviewerRequest) {
        log.info("update interviewer {}: {}", interviewerId, interviewerRequest);

        Interviewer updatedinterviewer = interviewerRepository.save(interviewerService.mapToDao(interviewerRequest, Optional.of(interviewerId)));
        log.info("put interviewer: {}", updatedinterviewer);
        return InterviewerResponse.map(updatedinterviewer);
    }


    @GetMapping("/initials/{initials}")
    public InterviewerResponse getByInitials(@PathVariable String initials) {
        return interviewerRepository.findByInitials(initials).map(InterviewerResponse::map).
                orElseThrow(() -> new ResourceNotFoundException("interviewer with initials " + initials + " does not exist"));
    }

    @CrossOrigin
    @PostMapping("/search")
    public ResponseEntity<?> searchSpecific(@RequestBody InterviewerSearchRequest searchRequest) {
        return new ResponseEntity<>(interviewerService.searchinterviewer(searchRequest), HttpStatus.OK);
    }

}

