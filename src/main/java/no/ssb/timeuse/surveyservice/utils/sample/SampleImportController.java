package no.ssb.timeuse.surveyservice.utils.sample;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import no.ssb.timeuse.surveyservice.exception.ResourceValidationException;
import no.ssb.timeuse.surveyservice.respondent.RespondentRepository;
import no.ssb.timeuse.surveyservice.respondent.RespondentService;
import no.ssb.timeuse.surveyservice.respondent.idmapper.RespondentIdMapperRepository;
import no.ssb.timeuse.surveyservice.respondent.idmapper.RespondentIdMapperService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/v1/sample_import")
public class SampleImportController {

    RespondentRepository respondentRepository;
    RespondentService respondentService;
    RespondentIdMapperRepository respondentIdMapperRepository;
    RespondentIdMapperService respondentIdMapperService;

    @CrossOrigin
    @PostMapping
    public void importRespondents(@RequestBody List<SampleImport> from) {

        // First do some validation and manipulation.
        List<SampleImport> updatedFrom = from;
        updatedFrom.stream().forEach(r -> {
            validateSampleImport(r);
            r.setNewUUID(UUID.randomUUID());
            if (!r.getCareOfAddress().isEmpty()) {
                if (!r.getAddress().isEmpty()) {
                    r.setAddress(r.getCareOfAddress() + ", " + r.getAddress());
                }
                else {
                    r.setAddress(r.getCareOfAddress());
                }
            }
        });

        // Create Respondent
        val newRespondents = updatedFrom.stream()
                .map(r -> respondentService.mapToRespondentFromSample(r))
                .collect(Collectors.toList());

        respondentRepository.saveAll(newRespondents);
        log.info("Respondents imported: " + newRespondents.size());

        // Map FNR to UUID (Respondent_mapper)
        val newMapper = updatedFrom.stream()
                .map(r -> respondentIdMapperService.mapFromSample(r))
                .collect(Collectors.toList());

        respondentIdMapperRepository.saveAll(newMapper);

    }

    public void validateSampleImport(SampleImport request) {
        try {
            Long.parseLong(request.getIoNumber());
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate.parse(request.getDateOfBirth(), dateTimeFormatter);
        } catch (Exception e) {
            throw new ResourceValidationException("SampleImport with ioNumber " + request.getIoNumber() + " is invalid");
        }
    }

}
