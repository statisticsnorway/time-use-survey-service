package no.ssb.timeuse.surveyservice.household;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import lombok.val;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.exception.ResourceValidationException;
import no.ssb.timeuse.surveyservice.exception.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Timed
@RequestMapping("/v1/households")
public class HouseholdController {
    private final HouseholdRepository repository;
    private final HouseholdService service;

    @CrossOrigin
    @GetMapping
    public List<HouseholdResponse> entries() {
        return repository.findAll().stream()
                .map(HouseholdResponse::map)
                .collect(Collectors.toList());
    }

    @CrossOrigin
    @GetMapping("/{ioNumber}")
    public HouseholdResponse getByIoNumber(@PathVariable Long ioNumber) {
        return repository.findByIoNumber(ioNumber).map(HouseholdResponse::map).
                orElseThrow(() -> new ResourceNotFoundException("Household with IO-number " + ioNumber + " does not exist"));
    }

    //TODO validering for Household
    @CrossOrigin
    @PutMapping("/{id}")
    public Household updateHousehold(@PathVariable Long id, @RequestBody HouseholdRequest request) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Household with id: " + id + " does not exist"));

        try {
            return repository.save(service.convertedToHousehold(id, request));
        } catch (ValidationException e) {
            throw new ResourceValidationException("Household not valid");
        }
    }

}
