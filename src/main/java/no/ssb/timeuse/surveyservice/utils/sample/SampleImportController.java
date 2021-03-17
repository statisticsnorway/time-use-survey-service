package no.ssb.timeuse.surveyservice.utils.sample;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import no.ssb.timeuse.surveyservice.exception.ResourceValidationException;
import no.ssb.timeuse.surveyservice.household.HouseholdRepository;
import no.ssb.timeuse.surveyservice.household.HouseholdService;
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
    HouseholdRepository householdRepository;
    HouseholdService householdService;
    RespondentIdMapperRepository respondentIdMapperRepository;
    RespondentIdMapperService respondentIdMapperService;

    @CrossOrigin
    @PostMapping
    public void importHouseholdsWithRespondents(@RequestBody List<SampleImport> from) {

        // First do some validation and manipulation.
        // Generates UUID here as it has to be in sync between Household and Respondent
        List<SampleImport> updatedFrom = from;
        updatedFrom.stream().forEach(r -> {
            validateSampleImport(r);
            r.setNewUUID(UUID.randomUUID());
            r.setIsReferencePerson(getStandardValueForBooleanstring(r.getIsReferencePerson()));
            if (!r.getCareOfAddress().isEmpty()) {
                if (!r.getAddress().isEmpty()) {
                    r.setAddress(r.getCareOfAddress() + ", " + r.getAddress());
                }
                else {
                    r.setAddress(r.getCareOfAddress());
                }
            }
        });

        // Create Household
        val newHouseholds = updatedFrom.stream()
                .filter(r -> r.getIsReferencePerson().equalsIgnoreCase("true"))
                .map(r -> householdService.mapToHouseholdFromSample(r))
                .collect(Collectors.toList());

        householdRepository.saveAll(newHouseholds);
        log.info("Households imported: " + newHouseholds.size());

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
            Long.parseLong(request.getHouseholdSequenceNumber());
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate.parse(request.getDateOfBirth(), dateTimeFormatter);
            Integer refWeek = Integer.parseInt(request.getReferenceWeek());
            if (refWeek < 1 || refWeek > 53 || !getStandardValueForBooleanstring(request.getIsReferencePerson()).matches("true|false")) {
                throw new ResourceValidationException("SampleImport with ioNumber " + request.getIoNumber() + " is invalid");
            }
        } catch (Exception e) {
            throw new ResourceValidationException("SampleImport with ioNumber " + request.getIoNumber() + " is invalid");
        }
    }

    public String getStandardValueForBooleanstring(String str) {
        if (str.toLowerCase().matches("true|sann|1")) {
            return "true";
        } else if (str.toLowerCase().matches("false|usann|0")) {
            return "false";
        }
        return str;
    }
}
