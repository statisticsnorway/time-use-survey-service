package no.ssb.timeuse.surveyservice.search;

import lombok.AllArgsConstructor;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.respondent.RespondentRepository;
import no.ssb.timeuse.surveyservice.respondent.RespondentResponse;
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

    public List<RespondentResponse> findRespondentsByGroupFilter(SearchRequestGroup group) {
        return respondentRepository.searchForRespondents(group).stream().map(RespondentResponse::map).collect(Collectors.toList());
    }



    public List<RespondentResponse> searchRespondentSpecific(SearchRequest searchRequest) {
        if (searchRequest.telephone != null) {
            return respondentRepository.findAllByPhoneIgnoreCase(searchRequest.getTelephone())
                    .stream().map((RespondentResponse::map))
                    .collect(Collectors.toList());
        } else if (searchRequest.email != null) {
            return respondentRepository.findAllByEmailContainingIgnoreCase(searchRequest.getEmail())
                    .stream().map((RespondentResponse::map))
                    .collect(Collectors.toList());
        }
        else if (searchRequest.name != null) {
            return respondentRepository.findAllByNameContainingIgnoreCase(searchRequest.getName())
                    .stream().map((RespondentResponse::map))
                    .collect(Collectors.toList());
        }

        return respondentRepository.findByIoNumber(searchRequest.getIoNumber())
                .map(RespondentResponse::map)
                .stream().collect(Collectors.toList());

    }


    // TODO: Use native query in repository for flexible WHERE <key> = <value>
    private List<Person> findByName() {
        return List.of(
                new Person("Tor", "12"),
                new Person("Bjørn", "34"),
                new Person("Hans", "56") );
    }


    private List<Person> findAll() {
        return List.of(
                new Person("Tor", "12"),
                new Person("Bjørn", "34"),
                new Person("Hans", "56") );
    }


}
