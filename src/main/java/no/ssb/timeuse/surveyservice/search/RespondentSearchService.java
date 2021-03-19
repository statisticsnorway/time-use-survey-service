package no.ssb.timeuse.surveyservice.search;

import lombok.AllArgsConstructor;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.respondent.RespondentRepository;
import no.ssb.timeuse.surveyservice.respondent.RespondentResponse;
import no.ssb.timeuse.surveyservice.respondent.Respondent;
import no.ssb.timeuse.surveyservice.respondent.RespondentRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RespondentSearchService {

    private final RespondentRepository respondentRepository;

    public List<RespondentResponse> search() {
        return respondentRepository.findAll().stream()
                .map(RespondentResponse::map)
                .collect(Collectors.toList());
    }

    public RespondentResponse searchRespondentSpecific(SearchRequest searchRequest) {
        if (searchRequest.telephone != null) {
            return respondentRepository.findByPhone(searchRequest.getTelephone())
                    .map(RespondentResponse::map)
                    .orElseThrow(() -> new ResourceNotFoundException("Respondent with telephone number " + searchRequest.getTelephone() + " does not exist"));
        } else if (searchRequest.email != null) {
            return respondentRepository.findByEmail(searchRequest.getEmail())
                    .map(RespondentResponse::map)
                    .orElseThrow(() -> new ResourceNotFoundException("Respondent with email " + searchRequest.getEmail() + " does not exist"));
        }
        return respondentRepository.findByIoNumber(searchRequest.getIoNumber())
                .map(RespondentResponse::map)
                .orElseThrow(() -> new ResourceNotFoundException("Respondent with ioNumber " + searchRequest.getIoNumber() + " does not exist"));

    }


    // TODO: Use native query in repository for flexible WHERE <key> = <value>
    private List<Person> findByFirstName() {
        return List.of(
                new Person("Tor", "Lindberg", "12"),
                new Person("Bjørn", "Kvernstuen", "34"),
                new Person("Hans", "Rotmo", "56"));
    }

    private List<Person> findByLastName() {
        return List.of(
                new Person("Tor", "Lindberg", "12"),
                new Person("Bjørn", "Lindberg", "34"),
                new Person("Hans", "Lindberg", "56"));
    }

    private List<Person> findAll() {
        return List.of(
                new Person("Tor", "Lindberg", "12"),
                new Person("Tor", "Kvernstuen", "34"),
                new Person("Tor", "Rotmo", "56"));
    }


}
